
(ns pretty-controls.renderers.side-effects)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-content!
  ; @description
  ; ...
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)
  ;  :renderer-id (keyword)}
  ;
  ; @usage
  ; (render-content! :my-content {:content     [:div "My content"]
  ;                               :renderer-id :my-renderer})
  [id props])

(defn remove-content!
  ; @description
  ; ...
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:renderer-id (keyword)}
  ;
  ; @usage
  ; (remove-content! :my-content {:renderer-id :my-renderer})
  [id props])
