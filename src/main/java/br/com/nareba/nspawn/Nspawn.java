package br.com.nareba.nspawn;

import br.com.nareba.nspawn.core.Spawn;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Nspawn extends PluginBase {
    private Map<String, Spawn> spawnList;
    private final String rootPermission = "nspawn";
    private final String spawnPermission = rootPermission + ".spawn";

    @Override
    public void onEnable() {
        try   {
            getLogger().info("Initializing Nspawn...");
            File file = new File(getDataFolder(), "spawns.json");
            if (file.exists())   {
                String fileData = Utils.readFile(file);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Type typeSpawnList = new TypeToken<Map<String, Spawn>>(){}.getType();
                this.spawnList = gson.fromJson(fileData, typeSpawnList);
            }else   {
                getDataFolder().mkdirs();
                file.createNewFile();
                this.spawnList = new HashMap<String, Spawn>();
            }
        }catch (Exception e)   {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Finalizing Nspawn...");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String lowerCaseCommand = command.getName().toLowerCase();
        if (lowerCaseCommand.equals("setspawn"))   {
            if (sender.isPlayer())   {
                Player player = (Player)sender;
                if (args.length == 0)   {
                    try   {
                        Position spawnPosDefault = player.getPosition();
                        Spawn defaultSpawn = new Spawn(spawnPosDefault, player.level.getName());
                        spawnList.put("default", defaultSpawn);
                        saveSpawnOnFile();
                        getLogger().info("Spawn 'default' has been saved!");
                        player.sendMessage("Spawn 'default' has been saved!");
                    }catch (Exception e)   {
                        e.printStackTrace();
                    }
                }else if (args.length == 1)   {
                    try   {
                        String spawnName = args[0];
                        Position spawnPos = player.getPosition();
                        Spawn newSpawn = new Spawn(spawnPos, player.level.getName());
                        spawnList.put(spawnName, newSpawn);
                        saveSpawnOnFile();
                        getLogger().info("Spawn '" + spawnName +"' has been saved!");
                        player.sendMessage("Spawn '" + spawnName +"' has been saved!");
                    }catch (Exception e)   {
                        e.printStackTrace();
                    }
                }
            }
        }else if (lowerCaseCommand.equals("spawn"))   {
            if (sender.isPlayer())   {
                Player player = (Player)sender;
                if (args.length == 0)   {
                    if (spawnList.containsKey("default"))   {
                        Spawn defaultSpawn = spawnList.get("default");
                        Level defaultLevel = getServer().getLevelByName(defaultSpawn.getLevelName());
                        Location defaultLocation = new Location();
                        defaultLocation.setLevel(defaultLevel);
                        defaultLocation.setComponents(defaultSpawn.getSpawnPosition().x, defaultSpawn.getSpawnPosition().y, defaultSpawn.getSpawnPosition().z);
                        player.teleportImmediate(defaultLocation);
                        player.sendMessage("You has been teleported!");
                    }
                }else if (args.length == 1)   {
                    String spawnName = args[0];
                    if (spawnList.containsKey(spawnName))   {
                        if (player.hasPermission(this.spawnPermission + "." + spawnName))   {
                            Spawn customSpawn = spawnList.get(spawnName);
                            Level customLevel = getServer().getLevelByName(customSpawn.getLevelName());
                            Location customLocation = new Location();
                            customLocation.setLevel(customLevel);
                            customLocation.setComponents(customSpawn.getSpawnPosition().x, customSpawn.getSpawnPosition().y, customSpawn.getSpawnPosition().z);
                            player.teleportImmediate(customLocation);
                            player.sendMessage("You has been teleported!");
                        }
                    }else   {
                        player.sendMessage("Spawn " + spawnName + " doesn't exists!");
                    }
                }
            }
        }else if (lowerCaseCommand.equals("delspawn"))   {
            if (sender.isPlayer())   {
                Player player = (Player)sender;
                if (args.length > 0)   {
                    String spawnName = args[0];
                    if (!spawnName.equals("default"))   {
                        if (spawnList.containsKey(spawnName))   {
                            spawnList.remove(spawnName);
                            saveSpawnOnFile();
                            player.sendMessage("spawn " + spawnName + " has been deleted!");
                        }
                    }else   {
                        player.sendMessage("You can't remove 'default' spawn!");
                    }
                }
            }
        }else if (lowerCaseCommand.equals("level"))   {
            if (sender.isPlayer())   {
                Player player = (Player)sender;
                if (args.length > 0)   {
                    String levelName = args[0];
                    Level findedLevel = getServer().getLevelByName(levelName);
                    if (findedLevel != null)   {
                        player.teleportImmediate(getServer().getLevelByName(levelName).getSpawnLocation().getLocation());
                        player.sendMessage("You has been teleported!");
                    }else   {
                        player.sendMessage("Can't find this level!");
                    }
                }
            }
        }
        return true;
    }
    public Map<String, Spawn> getSpawnList()   {
        return this.spawnList;
    }
    public void saveSpawnOnFile()   {
        try   {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Utils.writeFile(new File(getDataFolder(), "spawns.json"), gson.toJson(spawnList));
        }catch (Exception e)   {
            e.printStackTrace();
        }
    }
}

