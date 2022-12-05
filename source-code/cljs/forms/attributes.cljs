
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns forms.attributes
    (:require [css.api      :as css]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn form-block-attributes
  ; @param (map)(opt) options
  ; {:ratio (%)(opt)
  ;   Default: 100}
  ;
  ; @usage
  ; (form-block-attributes)
  ;
  ; @usage
  ; (form-block-attributes {...})
  ;
  ; @return (map)
  ; {:class (keyword)
  ;  :style (map)}
  ([]                (form-block-attributes {:ratio 100}))
  ([{:keys [ratio]}] (if-let [viewport-large? @(r/subscribe [:x.environment/viewport-large?])]
                             {:style {:min-width (css/percent ratio)} :class :forms--form-block}
                             {:style {:min-width (css/percent 100)}   :class :forms--form-block})))

(defn form-row-attributes
  ; @param (map)(opt) options
  ;
  ; @usage
  ; (form-row-attributes)
  ;
  ; @usage
  ; (form-row-attributes {...})
  ;
  ; @return (map)
  ; {:class (keyword)}
  ([]           (form-row-attributes {}))
  ([{:keys []}] {:class :forms--form-row}))

(defn form-column-attributes
  ; @param (map)(opt) options
  ;
  ; @usage
  ; (form-column-attributes)
  ;
  ; @usage
  ; (form-column-attributes {...})
  ;
  ; @return (map)
  ; {:class (keyword)}
  ([]           (form-column-attributes {}))
  ([{:keys []}] {:class :forms--form-column}))
