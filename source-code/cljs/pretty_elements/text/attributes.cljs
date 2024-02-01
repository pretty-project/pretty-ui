
(ns pretty-elements.text.attributes
    (:require [fruits.css.api                   :as css]
              [pretty-css.appearance.api        :as pretty-css.appearance]
              [pretty-css.basic.api             :as pretty-css.basic]
              [pretty-css.content.api           :as pretty-css.content]
              [pretty-css.layout.api            :as pretty-css.layout]
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
  (merge ;(label.attributes/copyable-attributes text-id text-props)
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
  (merge ;(label.attributes/content-attributes text-id text-props)
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
           (pretty-css.appearance/background-attributes           text-props)
           (pretty-css.appearance/border-attributes          text-props)
           (pretty-css.layout/flex-attributes          text-props)
           (pretty-css.layout/element-size-attributes    text-props)
           (pretty-css.content/font-attributes            text-props)
           (pretty-css.layout/indent-attributes          text-props)
           (pretty-css.content/selectable-text-attributes text-props)
           (pretty-css.basic/style-attributes           text-props))))

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
      (pretty-css.basic/class-attributes        text-props)
      (pretty-css.layout/outdent-attributes      text-props)
      (pretty-css.basic/state-attributes        text-props)
      (pretty-css.appearance/theme-attributes        text-props)
      (pretty-css.layout/wrapper-size-attributes text-props)))
