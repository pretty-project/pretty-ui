
(ns pretty-elements.vertical-polarity.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-body-attributes
  ; @ignore
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ; {:horizontal-align (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-horizontal-align (keyword)
  ;  :style (map)}
  [_ {:keys [horizontal-align style] :as polarity-props}]
  (-> {:class                 :pe-vertical-polarity--body
       :data-horizontal-align horizontal-align
       :style                 style}
      (pretty-css/indent-attributes polarity-props)))

(defn polarity-attributes
  ; @ignore
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;
  ; @return (map)
  ; {}
  [_ polarity-props]
  (-> {:class :pe-vertical-polarity}
      (pretty-css/default-attributes      polarity-props)
      (pretty-css/outdent-attributes      polarity-props)
      (pretty-css/element-size-attributes polarity-props)))
