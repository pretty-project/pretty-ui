
(ns elements.text.attributes
    (:require [css.api                   :as css]
              [pretty-css.api            :as pretty-css]
              [elements.label.attributes :as label.attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copyable-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {}
  [text-id text-props]
  (merge (label.attributes/copyable-attributes text-id text-props)
         {:class :e-text--copyable}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {}
  [text-id text-props]
  (merge (label.attributes/content-attributes text-id text-props)
         {:class :e-text--content}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-body-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:font-size (keyword)
  ;  :max-lines (integer)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-cropped (boolean)
  ;  :data-letter-spacing (keyword)
  ;  :style (map)}
  [text-id {:keys [font-size horizontal-align horizontal-position max-lines style] :as text-props}]
  (-> (if max-lines (let [line-height-var (css/var  (str "line-height-" (name font-size)))
                          height-calc     (css/calc (str max-lines" * "line-height-var))]
                         {:class                        :e-text--body
                          :data-cropped                 true
                          :data-horizontal-column-align horizontal-position
                          :data-horizontal-text-align   horizontal-align
                          :data-letter-spacing          :auto
                          :style (merge style {:max-height height-calc})})
                    (let []
                         {:class                        :e-text--body
                          :data-horizontal-column-align horizontal-position
                          :data-horizontal-text-align   horizontal-align
                          :style                        style
                          :data-letter-spacing          :auto}))
      (pretty-css/color-attributes  text-props)
      (pretty-css/font-attributes   text-props)
      (pretty-css/indent-attributes text-props)
      (pretty-css/text-attributes   text-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {}
  [_ text-props]
  (-> {:class :e-text}
      (pretty-css/default-attributes          text-props)
      (pretty-css/outdent-attributes          text-props)
      (pretty-css/element-min-size-attributes text-props)
      (pretty-css/element-size-attributes     text-props)))
