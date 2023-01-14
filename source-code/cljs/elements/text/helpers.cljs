
(ns elements.text.helpers
    (:require [css.api                :as css]
              [pretty-css.api         :as pretty-css]
              [elements.label.helpers :as label.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copyable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  [text-id text-props]
  (label.helpers/copyable-attributes text-id text-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  [text-id text-props]
  (label.helpers/content-attributes text-id text-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:font-size (keyword)
  ;  :max-lines (integer)(opt)
  ;  :selectable? (boolean)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-cropped (boolean)
  ;  :data-selectable (boolean)
  ;  :style (map)}
  [text-id {:keys [font-size horizontal-align max-lines selectable? style] :as text-props}]
  (-> (if max-lines (let [line-height-var (css/var  (str "line-height-" (name font-size)))
                          height-calc     (css/calc (str max-lines" * "line-height-var))]
                         {:data-cropped                 true
                          :data-horizontal-column-align horizontal-align
                          :data-horizontal-text-align   horizontal-align
                          :data-selectable              selectable?
                          :style (merge style {:max-height height-calc})})
                    (let []
                         {:data-selectable              selectable?
                          :data-horizontal-column-align horizontal-align
                          :data-horizontal-text-align   horizontal-align
                          :style                        style}))
      (pretty-css/color-attributes  text-props)
      (pretty-css/font-attributes   text-props)
      (pretty-css/indent-attributes text-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  [_ text-props]
  (-> {} (pretty-css/default-attributes text-props)
         (pretty-css/outdent-attributes text-props)))
