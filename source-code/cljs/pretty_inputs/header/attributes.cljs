
(ns pretty-inputs.header.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ header-props]
  (-> {:class :pi-header--inner}
      (pretty-attributes/flex-attributes        header-props)
      (pretty-attributes/inner-size-attributes  header-props)
      (pretty-attributes/inner-space-attributes header-props)
      (pretty-attributes/mouse-event-attributes header-props)
      (pretty-attributes/style-attributes       header-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {}
  [_ header-props]
  (-> {:class :pi-header}
      (pretty-attributes/class-attributes          header-props)
      (pretty-attributes/inner-position-attributes header-props)
      (pretty-attributes/outer-position-attributes header-props)
      (pretty-attributes/outer-size-attributes     header-props)
      (pretty-attributes/outer-space-attributes    header-props)
      (pretty-attributes/state-attributes          header-props)
      (pretty-attributes/theme-attributes          header-props)))
