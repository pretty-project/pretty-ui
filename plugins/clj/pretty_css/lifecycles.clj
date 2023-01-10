
(ns pretty-css.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.environment/merge-css! :pretty-css/style.css
                                              {:resources [; Animations
                                                           "public/pretty-css/animations/position.css"
                                                           "public/pretty-css/animations/size.css"
                                                           "public/pretty-css/animations/visiblity.css"
                                                           ; Presets
                                                           "public/pretty-css/presets/align.css"
                                                           "public/pretty-css/presets/animation.css"
                                                           "public/pretty-css/presets/badge.css"
                                                           "public/pretty-css/presets/bubble.css"
                                                           "public/pretty-css/presets/border.css"
                                                           "public/pretty-css/presets/color.css"
                                                           "public/pretty-css/presets/debug.css"
                                                           "public/pretty-css/presets/effect.css"
                                                           "public/pretty-css/presets/flex.css"
                                                           "public/pretty-css/presets/font.css"
                                                           "public/pretty-css/presets/icon.css"
                                                           "public/pretty-css/presets/indent.css"
                                                           "public/pretty-css/presets/input.css"
                                                           "public/pretty-css/presets/marker.css"
                                                           "public/pretty-css/presets/outdent.css"
                                                           "public/pretty-css/presets/position.css"
                                                           "public/pretty-css/presets/scroll.css"
                                                           "public/pretty-css/presets/selection.css"
                                                           "public/pretty-css/presets/shadow.css"
                                                           "public/pretty-css/presets/size.css"
                                                           "public/pretty-css/presets/state.css"
                                                           "public/pretty-css/presets/text.css"
                                                           ; Profiles
                                                           "public/pretty-css/profiles/border.css"
                                                           "public/pretty-css/profiles/color.css"
                                                           "public/pretty-css/profiles/font.css"
                                                           "public/pretty-css/profiles/icon.css"
                                                           "public/pretty-css/profiles/layout.css"
                                                           "public/pretty-css/profiles/shadow.css"
                                                           ; Themes
                                                           "public/pretty-css/themes/dark.css"
                                                           "public/pretty-css/themes/high-contrast.css"
                                                           "public/pretty-css/themes/light.css"]
                                               :weight 1}]})
