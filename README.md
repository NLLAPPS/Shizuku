# Shizuku

This is a fork of [thedjchi/Shizuku](https://github.com/thedjchi/Shizuku) by NLL Apps. If you are looking for the original version, please visit the [RikkaApps/Shizuku](https://github.com/RikkaApps/Shizuku) repository.

This fork is not intended for general use. Support only provided in the context of official NLL Apps products. If you are not a NLL Apps customer, please use the original or **thedjchi** version of **Shizuku**.

## Download from

[https://acr.app](https://acr.app).


### Building the App

- Clone with `git clone --recurse-submodules`
- Run gradle task `:manager:assembleDebug` or `:manager:assembleRelease`

The `:manager:assembleDebug` task generates a debuggable server. You can attach a debugger to `shizuku_server` to debug the server. In Android Studio, ensure `Run/Debug configurations > Always install with package manager` is checked, so that the server will use the latest code.

### Submitting Changes

1. Fork the repository
2. Create a feature branch (`git checkout -b branch-name`)
3. Make your changes
4. Commit your changes (`git commit -m 'Commit message'`)
5. Push to the branch (`git push origin branch-name`)
6. Open a Pull Request

## 📃 License

All code files in this project are licensed under [Apache 2.0](LICENSE)
