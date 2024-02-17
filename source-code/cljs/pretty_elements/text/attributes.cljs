
(ns pretty-elements.text.attributes
    (:require [fruits.css.api                   :as css]
              [pretty-attributes.api            :as pretty-attributes]
              [pretty-elements.label.attributes :as label.attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-content-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ text-props]
  ;(let [line-count (:max-lines text-props)]
  ;     (pretty-attributes/adaptive-text-height font-size line-height line-count)]

  (-> {:class :pe-text--content}
      (pretty-attributes/font-attributes text-props)
      (pretty-attributes/text-attributes text-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-body-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:font-size (keyword, px or string)
  ;  :max-lines (integer)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  ;  :data-cropped (boolean)
  ;  :data-letter-spacing (keyword)
  ;  :style (map)}
  [text-id {:keys [font-size max-lines] :as text-props}]




  (let [line-height-var (css/var  (str "line-height-" (name font-size)))
        height-calc     (css/calc (str max-lines" * "line-height-var))]
       (-> (if max-lines {:class               :pe-text--body
                          :data-cropped        true
                          :data-letter-spacing :auto
                          :style               {:max-height height-calc}}
                         {:class               :pe-text--body
                          :data-letter-spacing :auto})
           (pretty-attributes/background-color-attributes           text-props)
           (pretty-attributes/border-attributes          text-props)
           (pretty-attributes/flex-attributes          text-props)
           (pretty-attributes/indent-attributes          text-props)
           (pretty-attributes/size-attributes               text-props)
           (pretty-attributes/style-attributes           text-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ text-props]
  (-> {:class :pe-text}
      (pretty-attributes/class-attributes        text-props)
      (pretty-attributes/outdent-attributes      text-props)
      (pretty-attributes/state-attributes        text-props)
      (pretty-attributes/theme-attributes        text-props)
      (pretty-attributes/wrapper-size-attributes text-props)))
