
(ns elements.label.utils
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-copy-f
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:content (string)(opt)
  ;  :on-copy (Re-Frame metamorphic-event)}
  ;
  ; @return (function)
  [_ {:keys [content on-copy]}]
  ; XXX#7009 (source-code/cljs/elements/label/prototypes.cljs)
  (fn [_] (r/dispatch (r/metamorphic-event<-params on-copy content))))
