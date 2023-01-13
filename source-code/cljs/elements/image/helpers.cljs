
(ns elements.image.helpers
    (:require [dom.api               :as dom]
              [elements.image.config :as image.config]
              [pretty-css.api        :as pretty-css]
              [react.api             :as react]))

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
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:on-error (function)
  ;  :ref (?)
  ;  :style (map)}
  [image-id {:keys [style] :as image-props}]
  (merge (pretty-css/indent-attributes image-props)
         {:on-error (on-error-f           image-id)
          :ref      (react/set-reference! image-id)
          :style    style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (map)
  [_ image-props]
  (merge (pretty-css/default-attributes image-props)
         (pretty-css/outdent-attributes image-props)))
