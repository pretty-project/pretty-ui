
(ns components.vector-items-header.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-body-attributes
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:horizontal-align (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-horizontal-row-align (keyword)
  ;  :data-orientation (keyword)
  ;  :style (map)}
  [_ {:keys [horizontal-align style] :as header-props}]
  (-> {:data-orientation          :horizontal
       :data-horizontal-row-align horizontal-align
       :style                     style}
      (pretty-css/indent-attributes header-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-attributes
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [_ header-props]
  (-> {} (pretty-css/default-attributes header-props)
         (pretty-css/outdent-attributes header-props)))
