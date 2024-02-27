
(ns pretty-layouts.sidebar.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-content-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ sidebar-props]
  (-> {:class :pl-sidebar--content}
      (pretty-attributes/content-size-attributes sidebar-props)
      (pretty-attributes/overflow-attributes     sidebar-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ sidebar-props]
  (-> {:class :pl-sidebar--inner}
      (pretty-attributes/animation-attributes        sidebar-props)
      (pretty-attributes/background-color-attributes sidebar-props)
      (pretty-attributes/border-attributes           sidebar-props)
      (pretty-attributes/flex-attributes             sidebar-props)
      (pretty-attributes/inner-size-attributes       sidebar-props)
      (pretty-attributes/inner-space-attributes      sidebar-props)
      (pretty-attributes/style-attributes            sidebar-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ sidebar-props]
  (-> {:class :pl-sidebar}
      (pretty-attributes/class-attributes          sidebar-props)
      (pretty-attributes/inner-position-attributes sidebar-props)
      (pretty-attributes/outer-position-attributes sidebar-props)
      (pretty-attributes/outer-size-attributes     sidebar-props)
      (pretty-attributes/outer-space-attributes    sidebar-props)
      (pretty-attributes/state-attributes          sidebar-props)
      (pretty-attributes/theme-attributes          sidebar-props)))
