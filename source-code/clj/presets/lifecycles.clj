
(ns presets.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.environment/merge-css! :presets/style.css
                                              {:resources ["public/css/presets/align.css"
                                                           "public/css/presets/animations.css"
                                                           "public/css/presets/border.css"
                                                           "public/css/presets/click.css"
                                                           "public/css/presets/color.css"
                                                           "public/css/presets/debug.css"
                                                           "public/css/presets/flex.css"
                                                           "public/css/presets/font.css"
                                                           "public/css/presets/icon.css"
                                                           "public/css/presets/indent.css"
                                                           "public/css/presets/markers.css"
                                                           "public/css/presets/outdent.css"
                                                           "public/css/presets/scroll.css"
                                                           "public/css/presets/selection.css"
                                                           "public/css/presets/size.css"
                                                           "public/css/presets/state.css"]
                                               :weight 0}]})
