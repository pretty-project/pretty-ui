
(ns pretty-elements.label.utils
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-copy-f
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:content (metamorphic-content)(opt)
  ;  :content-value-f (function)
  ;  :on-copy (Re-Frame metamorphic-event)}
  ;
  ; @return (function)
  [_ {:keys [content content-value-f on-copy]}]
  (fn [_] (r/dispatch (r/metamorphic-event<-params on-copy (content-value-f content)))))
