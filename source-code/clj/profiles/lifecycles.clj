
(ns profiles.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.environment/merge-css! :profiles/style.css
                                              {:resources ["public/css/profiles/border.css"
                                                           "public/css/profiles/color.css"
                                                           "public/css/profiles/font.css"
                                                           "public/css/profiles/icon.css"
                                                           "public/css/profiles/layout.css"
                                                           "public/css/profiles/shadow.css"]
                                               :weight 0}]})
