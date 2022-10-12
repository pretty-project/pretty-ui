
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns forms.attributes
    (:require [mid-fruits.css :as css]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn form-block-attributes
  ; @param (map)(opt) options
  ;  {:ratio (%)(opt)
  ;    Default: 100}
  ;
  ; @usage
  ;  (forms/form-block-attributes)
  ;
  ; @usage
  ;  (forms/form-block-attributes {...})
  ;
  ; @return (map)
  ;  {:class (keyword)
  ;   :style (map)}
  ([]                (form-block-attributes {:ratio 100}))
  ([{:keys [ratio]}] {:style {:min-width (css/percent ratio)} :class :forms--form-block}))

(defn form-row-attributes
  ; @param (map)(opt) options
  ;
  ; @usage
  ;  (forms/form-row-attributes)
  ;
  ; @usage
  ;  (forms/form-row-attributes {...})
  ;
  ; @return (map)
  ;  {:class (keyword)}
  ([]           (form-row-attributes {}))
  ([{:keys []}] {:class :forms--form-row}))

(defn form-column-attributes
  ; @param (map)(opt) options
  ;
  ; @usage
  ;  (forms/form-column-attributes)
  ;
  ; @usage
  ;  (forms/form-column-attributes {...})
  ;
  ; @return (map)
  ;  {:class (keyword)}
  ([]           (form-column-attributes {}))
  ([{:keys []}] {:class :forms--form-column}))
