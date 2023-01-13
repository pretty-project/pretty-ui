
(ns elements.horizontal-polarity.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ; {style (map)(opt)
  ;  :vertical-align (keyword)}
  ;
  ; @return (map)
  ; {:data-vertical-align (keyword)
  ;  :style (map)}
  [_ {:keys [style vertical-align] :as polarity-props}]
  (merge (pretty-css/indent-attributes polarity-props)
         {:data-vertical-align vertical-align
          :style               style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;
  ; @return (map)
  [_ polarity-props]
  (merge (pretty-css/default-attributes polarity-props)
         (pretty-css/outdent-attributes polarity-props)))
