
(ns elements.ghost.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ; {:border-radius (keyword)(opt)
  ;  :height (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-border-radius (keyword)
  ;  :data-block-height (keyword)
  ;  :style (map)}
  [_ {:keys [border-radius height style] :as ghost-props}]
  (merge (pretty-css/indent-attributes ghost-props)
         {:data-border-radius border-radius
          :data-block-height  height
          :style              style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  [_ ghost-props]
  (merge (pretty-css/default-attributes ghost-props)
         (pretty-css/outdent-attributes ghost-props)))
