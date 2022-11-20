
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.image.helpers
    (:require [dom.api                  :as dom]
              [elements.element.helpers :as element.helpers]
              [elements.image.config    :as image.config]
              [plugins.react.api        :as react]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-error-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) image-id
  ;
  ; @return (function)
  [image-id]
  #(let [image (react/get-reference image-id)]
        (dom/set-element-attribute! image "src" image.config/ERROR-IMAGE)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;  {:style (map)(opt)}
  ;
  ; @return (map)
  ;  {:on-error (function)
  ;   :ref (?)
  ;   :style (map)}
  [image-id {:keys [style]}]
  {:on-error (on-error-f           image-id)
   :ref      (react/set-reference! image-id)
   :style style})

(defn image-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (map)
  ; {}
  [image-id image-props]
  (merge (element.helpers/element-default-attributes image-id image-props)
         (element.helpers/element-indent-attributes  image-id image-props)))
