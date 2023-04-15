
(ns elements.horizontal-polarity.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-body-attributes
  ; @ignore
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ; {:style (map)(opt)
  ;  :vertical-align (keyword)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-vertical-align (keyword)
  ;  :style (map)}
  [_ {:keys [style vertical-align] :as polarity-props}]
  (-> {:class               :e-horizontal-polarity--body
       :data-vertical-align vertical-align
       :style               style}
      (pretty-css/indent-attributes polarity-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-attributes
  ; @ignore
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;
  ; @return (map)
  ; {}
  [_ polarity-props]
  (-> {:class :e-horizontal-polarity}
      (pretty-css/default-attributes      polarity-props)
      (pretty-css/outdent-attributes      polarity-props)
      (pretty-css/element-size-attributes polarity-props)))
