
(ns pretty-elements.text.attributes
    (:require [fruits.css.api                   :as css]
              [pretty-build-kit.api                   :as pretty-build-kit]
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
  ; {:font-size (keyword, px or string)
  ;  :max-lines (integer)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-cropped (boolean)
  ;  :data-letter-spacing (keyword)
  ;  :style (map)}
  [text-id {:keys [font-size max-lines style] :as text-props}]
  (-> (if max-lines (let [line-height-var (css/var  (str "line-height-" (name font-size)))
                          height-calc     (css/calc (str max-lines" * "line-height-var))]
                         {:class                        :pe-text--body
                          :data-cropped                 true
                          :data-letter-spacing          :auto
                          :style (assoc style :max-height height-calc)})
                    (let []
                         {:class                        :pe-text--body
                          :style                        style
                          :data-letter-spacing          :auto}))
      (pretty-build-kit/border-attributes           text-props)
      (pretty-build-kit/color-attributes            text-props)
      (pretty-build-kit/column-attributes           text-props)
      (pretty-build-kit/element-min-size-attributes text-props)
      (pretty-build-kit/element-size-attributes     text-props)
      (pretty-build-kit/font-attributes             text-props)
      (pretty-build-kit/indent-attributes           text-props)
      (pretty-build-kit/text-attributes             text-props)))

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
      (pretty-build-kit/class-attributes        text-props)
      (pretty-build-kit/outdent-attributes      text-props)
      (pretty-build-kit/state-attributes        text-props)
      (pretty-build-kit/wrapper-size-attributes text-props)))
