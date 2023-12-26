
(ns pretty-elements.text.attributes
    (:require [fruits.css.api                   :as css]
              [pretty-css.api                   :as pretty-css]
              [pretty-elements.label.attributes :as label.attributes]))

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
         {:class :pe-text--copyable}))

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
         {:class :pe-text--content}))

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
                         {:class                        :pe-text--body
                          :data-cropped                 true
                          :data-horizontal-column-align horizontal-position
                          :data-horizontal-text-align   horizontal-align
                          :data-letter-spacing          :auto
                          :style (merge style {:max-height height-calc})})
                    (let []
                         {:class                        :pe-text--body
                          :data-horizontal-column-align horizontal-position
                          :data-horizontal-text-align   horizontal-align
                          :style                        style
                          :data-letter-spacing          :auto}))
      (pretty-css/border-attributes           text-props)
      (pretty-css/color-attributes            text-props)
      (pretty-css/font-attributes             text-props)
      (pretty-css/indent-attributes           text-props)
      (pretty-css/text-attributes             text-props)
      (pretty-css/element-min-size-attributes text-props)
      (pretty-css/element-size-attributes     text-props)))

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
  (-> {:class :pe-text}
      (pretty-css/class-attributes        text-props)
      (pretty-css/state-attributes        text-props)
      (pretty-css/outdent-attributes      text-props)
      (pretty-css/wrapper-size-attributes text-props)))
