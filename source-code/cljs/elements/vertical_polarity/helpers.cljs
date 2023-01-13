
(ns elements.vertical-polarity.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ; {:horizontal-align (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-horizontal-align (keyword)
  ;  :style (map)}
  [_ {:keys [horizontal-align style] :as polarity-props}]
  (merge (pretty-css/indent-attributes polarity-props)
         {:data-horizontal-align horizontal-align
          :style                 style}))

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
