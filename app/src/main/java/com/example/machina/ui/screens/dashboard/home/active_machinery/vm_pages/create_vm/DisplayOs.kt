package com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm

import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import com.example.machina.terminal.FullTerminalScreen

/**
 * Display OS - Full interactive terminal screen
 * Shows a Linux terminal interface similar to Termux
 * Appears after DownloadArtifacts in the VM creation flow
 */
@Composable
fun DisplayOs(
    navController: NavController
) {
    FullTerminalScreen(navController = navController)
}
