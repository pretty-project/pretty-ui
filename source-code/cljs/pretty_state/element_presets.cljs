
(ns pretty-state.element-presets
    (:require [pretty-presets.api :refer [reg-preset!]]
              [re-frame.extra.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(reg-preset! :pretty-state/clickable-element
  ; @ignore
  ;
  ; @param (map) element-props
  ; {}
  ;
  ; @return (map)
  ; {}
  (fn [{:keys [on-click on-mouse-over on-right-click] :as element-props}]
      (merge (if on-click       {:on-click-f       #(r/dispatch on-click)})
             (if on-mouse-over  {:on-mouse-over-f  #(r/dispatch on-mouse-over)})
             (if on-right-click {:on-right-click-f #(r/dispatch on-click)})
             (-> element-props))))
