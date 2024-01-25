
(ns pretty-elements.text.attributes
    (:require [fruits.css.api                   :as css]
              [pretty-css.api :as pretty-css]
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
  ;  :max-lines (integer)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
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
           (pretty-css/border-attributes          text-props)
           (pretty-css/color-attributes           text-props)
           (pretty-css/column-attributes          text-props)
           (pretty-css/element-size-attributes    text-props)
           (pretty-css/font-attributes            text-props)
           (pretty-css/indent-attributes          text-props)
           (pretty-css/selectable-text-attributes text-props)
           (pretty-css/style-attributes           text-props))))

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
      (pretty-css/outdent-attributes      text-props)
      (pretty-css/state-attributes        text-props)
      (pretty-css/wrapper-size-attributes text-props)))
