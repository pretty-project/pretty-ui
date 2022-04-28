
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.form-a.helpers
    (:require [mid-fruits.css :as css]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn form-block-attributes
  ; @param (map)(opt) options
  ;  {:ratio (%)(opt)
  ;    Default: 100}
  ;
  ; @usage
  ;  (layouts/form-block-attributes)
  ;
  ; @usage
  ;  (layouts/form-block-attributes {...})
  ;
  ; @return (map)
  ;  {:class (keyword)
  ;   :style (map)}
  ([]                (form-block-attributes {:ratio 100}))
  ([{:keys [ratio]}] {:style {:min-width (css/percent ratio)} :class :x-form-a--form-block}))

(defn form-row-attributes
  ; @param (map)(opt) options
  ;
  ; @usage
  ;  (layouts/form-row-attributes)
  ;
  ; @usage
  ;  (layouts/form-row-attributes {...})
  ;
  ; @return (map)
  ;  {:class (keyword)}
  ([]           (form-row-attributes {}))
  ([{:keys []}] {:class :x-form-a--form-row}))

(defn form-column-attributes
  ; @param (map)(opt) options
  ;
  ; @usage
  ;  (layouts/form-column-attributes)
  ;
  ; @usage
  ;  (layouts/form-column-attributes {...})
  ;
  ; @return (map)
  ;  {:class (keyword)}
  ([]           (form-column-attributes {}))
  ([{:keys []}] {:class :x-form-a--form-column}))
