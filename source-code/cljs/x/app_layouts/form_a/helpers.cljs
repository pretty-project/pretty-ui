
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.form-a.helpers
    (:require [mid-fruits.css :as css]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-block-attributes
  ; @param (map)(opt) options
  ;  {:ratio (%)(opt)
  ;    Default: 50}
  ;
  ; @usage
  ;  (layouts/input-block-attributes)
  ;
  ; @usage
  ;  (layouts/input-block-attributes {...})
  ;
  ; @return (map)
  ;  {:class (keyword)
  ;   :style (map)}
  ([]                (input-block-attributes {:ratio 50}))
  ([{:keys [ratio]}] {:style {:min-width (css/percent ratio)} :class :x-form-a--input-block}))

(defn input-row-attributes
  ; @param (map)(opt) options
  ;
  ; @usage
  ;  (layouts/input-row-attributes)
  ;
  ; @usage
  ;  (layouts/input-row-attributes {...})
  ;
  ; @return (map)
  ;  {:class (keyword)}
  ([]           (input-row-attributes {}))
  ([{:keys []}] {:class :x-form-a--input-row}))

(defn input-column-attributes
  ; @param (map)(opt) options
  ;
  ; @usage
  ;  (layouts/input-column-attributes)
  ;
  ; @usage
  ;  (layouts/input-column-attributes {...})
  ;
  ; @return (map)
  ;  {:class (keyword)}
  ([]           (input-column-attributes {}))
  ([{:keys []}] {:class :x-form-a--input-column}))
