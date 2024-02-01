
(ns pretty-inputs.label.utils)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-copy-f
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:content (metamorphic-content)(opt)
  ;  :content-value-f (function)
  ;  :on-copy-f (function)}
  ;
  ; @return (function)
  [_ {:keys [content content-value-f on-copy-f]}]
  (fn [_] (let [label-content (content-value-f content)]
               (on-copy-f label-content))))
