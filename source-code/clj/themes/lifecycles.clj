
(ns themes.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.environment/merge-css! :themes/style.css
                                              {:resources ["public/css/themes/dark.css"
                                                           "public/css/themes/high-contrast.css"
                                                           "public/css/themes/light.css"]}]})
