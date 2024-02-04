
(ns pretty-elements.expandable.attributes
    (:require [pretty-css.appearance.api               :as pretty-css.appearance]
              [pretty-css.basic.api                    :as pretty-css.basic]
              [pretty-css.layout.api                   :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-body-attributes
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ expandable-props]
  (-> {:class :pe-expandable--body}
      (pretty-css.basic/style-attributes              expandable-props)
      (pretty-css.layout/double-block-size-attributes expandable-props)
      (pretty-css.layout/indent-attributes            expandable-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-attributes
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ expandable-props]
  (-> {:class :pe-expandable}
      (pretty-css.appearance/theme-attributes    expandable-props)
      (pretty-css.basic/class-attributes         expandable-props)
      (pretty-css.basic/state-attributes         expandable-props)
      (pretty-css.layout/outdent-attributes      expandable-props)
      (pretty-css.layout/wrapper-size-attributes expandable-props)))
