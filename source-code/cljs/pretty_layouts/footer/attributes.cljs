
(ns pretty-layouts.footer.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-body-attributes
  ; @ignore
  ;
  ; @param (keyword) footer-id
  ; @param (map) footer-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [footer-id footer-props]
  (-> {:class :pl-footer--body}
      (pretty-attributes/background-color-attributes footer-props)
      (pretty-attributes/border-attributes           footer-props)
      (pretty-attributes/flex-attributes             footer-props)
      (pretty-attributes/indent-attributes           footer-props)
      (pretty-attributes/inner-size-attributes       footer-props)
      (pretty-attributes/style-attributes            footer-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-attributes
  ; @ignore
  ;
  ; @param (keyword) footer-id
  ; @param (map) footer-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ footer-props]
  (-> {:class :pl-footer}
      (pretty-attributes/class-attributes      footer-props)
      (pretty-attributes/outdent-attributes    footer-props)
      (pretty-attributes/outer-size-attributes footer-props)
      (pretty-attributes/state-attributes      footer-props)
      (pretty-attributes/theme-attributes      footer-props)))
