#!/bin/bash
# Verification Script - Machina Termux Terminal Integration

echo "=============================================="
echo "Machina App - Termux Integration Verification"
echo "=============================================="
echo ""

PASS=0
FAIL=0

# Helper function
check_file() {
    local file=$1
    local description=$2
    
    if [ -f "$file" ]; then
        echo "✓ $description"
        echo "  Path: $file"
        ((PASS++))
    else
        echo "✗ $description"
        echo "  Path: $file (NOT FOUND)"
        ((FAIL++))
    fi
    echo ""
}

check_dir() {
    local dir=$1
    local description=$2
    
    if [ -d "$dir" ]; then
        echo "✓ $description"
        echo "  Path: $dir"
        ((PASS++))
    else
        echo "✗ $description"
        echo "  Path: $dir (NOT FOUND)"
        ((FAIL++))
    fi
    echo ""
}

check_grep() {
    local file=$1
    local pattern=$2
    local description=$3
    
    if grep -q "$pattern" "$file" 2>/dev/null; then
        echo "✓ $description"
        echo "  File: $file"
        ((PASS++))
    else
        echo "✗ $description"
        echo "  File: $file (pattern not found)"
        ((FAIL++))
    fi
    echo ""
}

echo "📋 CHECKING INTEGRATION FILES"
echo "=============================================="
echo ""

check_file "app/src/main/java/com/example/machina/terminal/TermuxTerminalScreen.kt" \
    "Termux Terminal Screen Component"

check_file "app/src/main/java/com/example/machina/terminal/TermuxIntegrationManager.kt" \
    "Termux Integration Manager"

check_grep "app/src/main/java/com/example/machina/ui/navigation/AppNavGraph.kt" \
    "TermuxTerminalScreen" \
    "Navigation Route Updated"

check_grep "app/src/main/AndroidManifest.xml" \
    "WAKE_LOCK" \
    "Manifest Permissions Added"

echo ""
echo "📋 CHECKING GRADLE CONFIGURATION"
echo "=============================================="
echo ""

check_grep "app/build.gradle.kts" \
    "termux-shared" \
    "Termux Dependencies Configured"

check_grep "app/build.gradle.kts" \
    "desugar_jdk_libs:2.1.5" \
    "Desugar Library Version Fixed"

check_grep "settings.gradle.kts" \
    "terminal-emulator" \
    "Module Paths Configured"

echo ""
echo "📋 CHECKING DOCKER FILES"
echo "=============================================="
echo ""

check_file "Dockerfile" \
    "Dockerfile Created"

check_file "docker-compose.build.yml" \
    "Docker Compose Build File"

check_file ".dockerignore" \
    "Docker Ignore File"

check_grep "Dockerfile" \
    "android-builder" \
    "Multi-Stage Build Configured"

echo ""
echo "📋 CHECKING BUILD SCRIPTS"
echo "=============================================="
echo ""

check_file "build-local.sh" \
    "Local Build Script"

check_file "build-docker.sh" \
    "Docker Build Script"

check_file "BUILD_GUIDE.md" \
    "Build Guide Documentation"

check_file "QUICK_REFERENCE.md" \
    "Quick Reference Documentation"

echo ""
echo "📋 CHECKING TERMUX MODULES"
echo "=============================================="
echo ""

check_dir "termux-kotlin-app/terminal-emulator" \
    "Terminal Emulator Module"

check_dir "termux-kotlin-app/terminal-view" \
    "Terminal View Module"

check_dir "termux-kotlin-app/termux-shared" \
    "Termux Shared Module"

echo ""
echo "=============================================="
echo "VERIFICATION SUMMARY"
echo "=============================================="
echo ""
echo "✓ Passed: $PASS"
echo "✗ Failed: $FAIL"
echo ""

if [ $FAIL -eq 0 ]; then
    echo "✓ All checks passed! Integration is complete."
    echo ""
    echo "Next steps:"
    echo "  1. Build locally: ./build-local.sh"
    echo "  2. Or build in Docker: ./build-docker.sh"
    echo "  3. Install on device: adb install -r <apk-path>"
    exit 0
else
    echo "✗ Some checks failed. Review the output above."
    exit 1
fi
