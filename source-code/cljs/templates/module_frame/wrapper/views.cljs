
(ns templates.module-frame.wrapper.views
    (:require [re-frame.api     :as r]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn wrapper
  ; @param (keyword) module-id
  ; @param (map) wrapper-props
  ; {:content (metamorphic content)
  ;  :menu (metamorphic content)(opt)}
  [module-id {:keys [content menu]}]
  [:div#t-module-frame (if-let [viewport-min? @(r/subscribe [:x.environment/viewport-min? 720])]
                               [x.components/content module-id menu])
                       [x.components/content module-id content]])
