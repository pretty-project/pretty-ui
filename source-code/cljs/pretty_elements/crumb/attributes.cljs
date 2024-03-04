
(ns pretty-elements.crumb.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inner-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pe-crumb--inner}
      (pretty-attributes/anchor-attributes          props)
      (pretty-attributes/clickable-state-attributes props)
      (pretty-attributes/effect-attributes          props)
      (pretty-attributes/flex-attributes            props)
      (pretty-attributes/inner-size-attributes      props)
      (pretty-attributes/inner-space-attributes     props)
      (pretty-attributes/mouse-event-attributes     props)
      (pretty-attributes/state-attributes           props)
      (pretty-attributes/style-attributes           props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn outer-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pe-crumb--outer}
      (pretty-attributes/class-attributes          props)
      (pretty-attributes/inner-position-attributes props)
      (pretty-attributes/outer-position-attributes props)
      (pretty-attributes/outer-size-attributes     props)
      (pretty-attributes/outer-space-attributes    props)
      (pretty-attributes/state-attributes          props)
      (pretty-attributes/theme-attributes          props)))
