
(ns components.compact-list-header.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-body-attributes
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [style] :as header-props}]
  (-> {:style style}
      (pretty-css/border-attributes header-props)
      (pretty-css/color-attributes  header-props)
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
