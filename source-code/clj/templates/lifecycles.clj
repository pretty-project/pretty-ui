
(ns templates.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.environment/merge-css! :templates/style.css
                                              {:resources ["public/css/templates/item-handler.css"
                                                           "public/css/templates/item-lister.css"
                                                           "public/css/templates/item-picker.css"
                                                           "public/css/templates/item-selector.css"
                                                           "public/css/templates/module-frame.css"
                                                           "public/css/templates/static-page.css"]}]})
