
(ns templates.board-frame.wrapper.views
    (:require [re-frame.api     :as r]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn wrapper
  ; @param (keyword) board-id
  ; @param (map) wrapper-props
  ; {:content (metamorphic content)
  ;  :menu (metamorphic content)(opt)}
  [board-id {:keys [content menu]}]
  [:div#t-board-frame (if-let [viewport-min? @(r/subscribe [:x.environment/viewport-min? 720])]
                              [x.components/content board-id menu])
                      [x.components/content board-id content]])
