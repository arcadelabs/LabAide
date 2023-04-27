## Dependancy information

[![LabAide version](https://repo.arcadelabs.in/api/badge/latest/releases/in/arcadelabs/labaide/LabAide?color=40c14a&name=LabAide%20version)](https://github.com/arcadelabs/LabAide/releases/latest)
[![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/arcadelabs/labaide/deploy.yml?branch=master&color=45b94e)](https://repo.arcadelabs.in/#/releases/in/arcadelabs/labaide/LabAide)

### Maven

> ```xml
> <repository>
>     <id>arcadelabs</id>
>     <name>ArcadeLabs Maven Repository</name>
>     <url>https://repo.arcadelabs.in/releases</url>
> </repository>
> ```

Artifacts (artifactId): [`common-aide`](https://github.com/arcadelabs/LabAide/tree/master/common-aide),
[`cooldown-aide`](https://github.com/arcadelabs/LabAide/tree/master/cooldown-aide),
[`update-aide`](https://github.com/arcadelabs/LabAide/tree/master/update-aide),
[`items-aide`](https://github.com/arcadelabs/LabAide/tree/master/items-aide)

> ```xml
> <dependency>
>     <groupId>in.arcadelabs.labaide</groupId>
>     <artifactId>[ARTIFACT]</artifactId>
>     <version>[VERSION]</version>
> </dependency>
> ```

### Gradle

> ```groovy
> maven {
>     name 'arcadelabsReleases'
>     url 'https://repo.arcadelabs.in/releases'
> }
> ```

> ```groovy
> dependencies {
>     implementation 'in.arcadelabs.labaide:[ARTIFACT]:[VERSION]'
> }
> ```

<br>

## This project is undergoing a major refactor! please wait for proper documentation before using this in your projects.
## Following examples are outdated.
### Utilities

```
.
└── in.arcadelabs.labaide
    ├── cooldown
    │   └── CooldownManager
    │       └── Cooldown management utility.
    ├── experience
    │   └── ExperienceManager
    │       └── Player experience management utility.
    ├── item
    │   ├── HeadBuilder
    │   │   └── Playuer skull bulider utility.
    │   └── ItemBuilder
    │       └── Item builder utility.
    ├── logger
    │   └── Logger
    │       └── Java logging utiliy supporting Adventure's components.
    ├── metrics
    │   └── Bstats
    │       └── Bstats metrics tracker.
    ├── namespacedkey
    │   └── NamespacedKeyBuilder
    │       └── NamespacedKey builder utility.
    └── randomizer
        └── ProbabilityCollection
            └── Weighted randomized collection utility.
```

### Examples

> Cooldown.java

```java
int cooldown=10; //Add cooldown peeriod in seconds
        CooldownManager cooldownManager=new CooldownManager(cooldown);
        MiniMessage miniMessage=MiniMessage.builder().build();

public void onCraftEvent(final CraftItemEvent event){
        if(!(Objects.equals(event.getRecipe().getResult(),new ItemStack(Material.NETHER_STAR))))return;
        Player player=(Player)event.getWhoClicked();
        if(!this.cooldownManager.isOnCooldown(player.getUniqueId())){
        event.getInventory().setResult(new ItemStack(Material.BEACON));
        if(this.cooldown>=0) // int value -1 will disable this
        this.cooldownManager.setCooldown(player.getUniqueId());
        }else{
        player.sendMessage(this.miniMessage.deserialize("<red>You can't craft beacon for another <seconds> seconds.</red>",
        Placeholder.component("seconds",Component.text(this.cooldownManager.getRemainingTime(player.getUniqueId())))));
        event.setCancelled(true);
        }
        }
```

> ExperienceManager.java

```java
  public void saveXP(final Player player){
        player.getPersistentDataContainer().set(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("player_xp"),
        PersistentDataType.INTEGER,
        ExperienceManager.getExp(player)); //Get precise amount of player XP 
        player.setExp(0);
        player.setLevel(0);
        }

public void restoreXP(final Player player){
        if(!(player.getPersistentDataContainer().has(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("player_xp"))))return;
        ExperienceManager.changeExp(player,
        player.getPersistentDataContainer().get(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("player_xp"),
        PersistentDataType.INTEGER));
        }
```

> HeadBuilder.java & ItemBulder.java

```java
  Logger logger=new Logger("LifeSteal",null,null,null);
        HeadBuilder headBuilder=new HeadBuilder(this.logger,Logger.Level.ERROR);
        //https://minecraft-heads.com/custom-heads/search?searchword=heart
        //Go down and check for 'value' fiels and copy paste that down below.
        String skullTexture="eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQzNmMzMjkxZmUwMmQxNDJjNGFmMjhkZjJmNTViYjAzOTdlMTk4NTU0ZTgzNDU5OTBkYmJjZDRjMTQwMzE2YiJ9fX0=";
        ItemBuilder itemBuilder=new ItemBuilder(Material.PLAYER_HEAD,lifeSteal.getHeadBuilder().createSkullMap(skullTexture))
        .setName(Component.text("Heart <3"))
        .setLore((List<Component>)Component.empty())
        .setModelData(389645)
        .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_item"),PersistentDataType.STRING,"No heart spoofing, dum dum.")
        .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_itemtype"),PersistentDataType.STRING,"Cursed")
        .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_itemindex"),PersistentDataType.STRING,"1")
        .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_healthpoints"),PersistentDataType.DOUBLE,"4")
        .build();
        ItemStack heart=itemBuilder.getBuiltItem();
```

> Logger.java

```java
  Logger logger=new Logger("LifeSteal",null,null,null);
        try{
        somethingStupid();
        }catch(StupidException e){
        logger.log(Logger.Level.ERROR,Component.text(e.getMessage(),NamedTextColor.DARK_PURPLE),e.fillInStackTrace());
        }
```

> Logger.java

```java
  BStats metrics=new BStats(this.instance,resourceID);
```

> NamespacedKeyBuilder.java

```java
  NamespacedKeyBuilder namespacedKeyBuilder=new NamespacedKeyBuilder("lifesteal",this.instance);
        NamespacedKey namespacedKey=namespacedKeyBuilder.getNewKey("heart_healthpoints");
```

> ProbabilityCollection.java

```java
    ProbabilityCollection<Character> randomChar=new ProbabilityCollection<>();
        randomChar.add('A',10);
        randomChar.add('B',10);
        randomChar.add('C',10);
        randomChar.add('D',10);

        char letter=randomChar.get();
```
