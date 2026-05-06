# 🚀 Create: Space and Beyond

> A NeoForge 1.21.1 mod expanding **Create** and **Create Aeronautics** into full space exploration — engines, atmospheres, orbit, and beyond.

---

## ⚠️ Development Status

**Early Alpha — Work In Progress**

| System | Status |
|---|---|
| Engine & Nozzle blocks | 🟡 In progress |
| Fluid Pipe (physics) | 🟡 In progress |
| Cosmonaut Suit | 🟡 In progress |
| Atmosphere / Oxygen system | 🟡 In progress |
| Zero Gravity | 🟡 In progress |
| Thermal Shield / Solar Coating | 🔴 Not started |
| Sealed Interior / Air system | 🔴 Not started |
| Orbit Dimension & Transition | 🔴 Not started |
| Space Navigation GUI | 🔴 Not started |
| Décor blocks | 🔴 Not started |
| Magnetic Boot Upgrade | 🔴 Not started |
| Spatial Locator | 🔴 Not started |

> **Textures and 3D models are not included.** Placeholder JSON models are generated at the correct resource paths — contributors must supply their own assets.

---

## 📦 What This Mod Adds

### 🔧 Machines & Functional Blocks

#### Engine Block
A multi-input kinetic engine for spacecraft propulsion.
- Requires **Fuel** (any `forge:fuels` fluid), **Water** (coolant), and **Air** (Create Aeronautics atmospheric input) on distinct block faces
- Dynamic Air-to-Fuel Ratio (AFR) controlled by Redstone signal (0–15 = 0–100% throttle)
- Temperature is persistent: rises with usage, drops with adequate coolant
- Block tint shifts grey → red as temperature increases
- Overheating → stall (pop effect) → config-defined cooldown before restart
- Power degrades proportionally to atmospheric oxygen density at high altitude
- In vacuum: no convective cooling → faster temperature rise
- Requires at least one adjacent Nozzle block to operate

#### Nozzle Block
Thrust output device, reads Create Aeronautics atmospheric pressure and density.
- Adjacent nozzles auto-merge into multiblock arrays: 1×1, 2×2, 3×3
- Length is configurable: longer = optimised for vacuum/high altitude, shorter = optimised for sea level
- Each size + length combination has an optimal pressure envelope; operating outside it reduces efficiency
- Tint shifts cool grey → orange → white with temperature

#### Air Recycler
Rotational machine (Create KineticBlockEntity) that replenishes sealed ship interior air.
- Requires minimum RPM (configurable) to activate
- Refill rate proportional to RPM
- Crafted from Water Breathing Splash Potions + Create components
- Runtime compatibility hooks for life-support mods

#### Spatial Locator
Block with GUI displaying current XYZ coordinates.
- Origin set via GUI or Sneak + right-click on any block
- Origin stored in BlockEntity NBT
- Exposes coordinate data via capability for other systems

---

### 🪨 Structural & Thermal Blocks

#### Thermal Shield Block
Slab-type reinforced hull block.
- Max temperature threshold stored server-side
- Tint shifts grey → orange → white during re-entry heating
- Exceeds threshold → block destroyed (no drop, or damaged variant drop)
- Temperature dissipates over time when not exposed to heating

#### Interior & Space Décor Blocks
Full set of decorative spacecraft interior blocks: panels, floors, walls, lighting.
- Variants: slabs, stairs, full blocks, trapdoors
- Metallic / futuristic aesthetic (placeholder textures — user supplies finals)
- Blast resistance and hardness tuned for hull material

---

### 🧰 Tools & Consumables

#### Solar Coating
Consumable right-click tool.
- Right-click any block → applies coating stored in NBT
- Coated blocks become immune to Create Aeronautics re-entry heating and radiation
- Shows visual indicator on coated blocks (overlay or particles)
- Has durability; crafted via Create processing machines from heat-resistant materials (copper, iron, gold, netherite scraps, blaze powder)

#### Thermal Seal
Right-click tool that defines a hermetically sealed ship interior.
- Applied blocks tracked persistently via world SavedData
- Holding item highlights all sealed blocks using Minecraft's outline render system
- Sealed volume traps air; total supply proportional to interior volume
- Air depletes faster with player activity (sprinting, combat)
- Screen vignette darkens progressively as air runs low
- Has durability

#### Magnetic Boot Upgrade
Upgrade item applied to any vanilla boots under a Create Mechanical Press.
- Sneak + contact on metallic ship surface → activates magnetic grip (cancels gravity offset)
- No custom armor class required; works via capability on vanilla boots
- Crafted from copper + lodestone (or custom magnetite ore)

---

### 🧑‍🚀 Cosmonaut Suit

Full 4-piece armor set (`helmet`, `chestplate`, `leggings`, `boots`).
- Complete set → full vacuum survival: no oxygen loss, no re-entry damage, reduced radiation exposure
- Crafted from mod-specific materials + Create-processed components
- Placeholder textures — user supplies finals

---

### ⚙️ Core Game Systems

#### Atmosphere & Altitude
- Above Y=300 (configurable), oxygen density drops
- Progressive suffocation damage scaled to oxygen deficit
- Engine power degrades proportionally to oxygen density (0% = engine off)
- No ambient sound in vacuum except inside sealed interiors
- FOV narrows smoothly when entering a sealed interior from vacuum

#### Zero Gravity
- Triggered above vacuum altitude threshold
- Cancels player gravity each tick
- 6-DOF directional input
- Slow fluid fly-mode movement on non-Hard difficulty
- Push-off impulse when contacting block surfaces

#### Orbit Dimension Transition
- Approaching Minecraft's Y ceiling triggers transition to custom **Orbit dimension** (no terrain, star skybox, planet sphere below)
- Custom full-screen overlay renderer: atmospheric glow → black → stars appearing
- No standard loading screen

#### Space Navigation
- GUI map: planet positions, ship trajectory, maneuver nodes (KSP-inspired, survival-friendly)
- Burn calculation: player sets thrust direction + duration → mod computes delta-V and trajectory arc
- Time warp for interplanetary legs
- Planet approach: cinematic fade into sphere of influence

---

## 🔗 Dependencies

| Mod | Type | Link |
|---|---|---|
| NeoForge 1.21.1 | Required | [neoforged.net](https://neoforged.net) |
| Create (NeoForge 1.21.1 build) | Required | [Modrinth / CurseForge](https://modrinth.com/mod/create) |
| Create Aeronautics | Required | [GitHub](https://github.com/Creators-of-Aeronautics/Simulated-Project) |

> No other mod dependencies are needed. All crafting recipes use only **Vanilla**, **Create**, and **Create Aeronautics** items.

---

## 🛠️ For Developers / Contributors

### Environment Setup

```bash
# Clone the repo
git clone https://github.com/YOUR_USER/space_engines.git
cd space_engines

# Build (always use the Gradle wrapper, never a system Gradle install)
./gradlew build

# Run the dev client
./gradlew runClient
```

> ⚠️ **Never modify the Gradle version in `gradle-wrapper.properties`** unless the NeoForge MDK changelog explicitly requires it.

### Code Conventions

- **NeoForge 1.21.1 patterns only**: use `@EventBusSubscriber` / `@SubscribeEvent`. Never use legacy Forge patterns.
- **Block registration**: `DeferredRegister` for all blocks, items, block entities, and dimensions.
- **BlockEntity persistence**: state via `saveAdditional` / `loadAdditional` NBT.
- **Fluid handling**: NeoForge `IFluidHandler` capability exclusively.
- **Rotational integration**: Create's `IRotate` / `KineticBlockEntity` API.
- **Create Aeronautics API**: always guard calls with `ModList.get().isLoaded("create_aeronautics")`.

### Configuration

All numerical thresholds are exposed in `config/spaceengines-common.toml`:
- Altitude limits
- AFR valid range
- Temperature caps
- Cooldown durations
- Minimum RPM for Air Recycler
- Oxygen density drop start Y level

### Assets / Models

Placeholder JSON models and empty texture references are generated at correct resource paths. **You must supply all textures and 3D models manually.** Do not open a PR with missing or AI-generated textures.

### Mod ID & Package

```
ModID  : space_engines
Package: com.yourname.spaceengines
```

---

## 📋 For Players

### Installation

1. Install **NeoForge 1.21.1**
2. Install **Create** (NeoForge 1.21.1 build)
3. Install **Create Aeronautics**
4. Drop `space_engines-x.x.x.jar` into your `mods/` folder
5. Launch

### Quick Start

1. Build a ship with Create Aeronautics contraption tools
2. Attach an **Engine Block** — connect Fuel, Water, and Air fluid inputs on the correct faces
3. Place at least one **Nozzle Block** adjacent to the engine
4. Use a **Thermal Seal** tool to mark your ship interior as hermetically sealed
5. Suit up with the **Cosmonaut Suit** for vacuum survival
6. Throttle up via Redstone signal and breach the atmosphere

---

## 📄 License

*To be defined.*

---

## 🤝 Contributing

Issues and PRs welcome. Please read the developer section above before submitting code.
If you are a 3D artist or texture artist, contributions of assets are especially welcome — open an issue to discuss.
