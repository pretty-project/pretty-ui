
(ns pretty-inputs.header.attributes
    (:require [pretty-css.appearance.api          :as pretty-css.appearance]
              [pretty-css.basic.api               :as pretty-css.basic]
              [pretty-css.content.api             :as pretty-css.content]
              [pretty-css.layout.api              :as pretty-css.layout]
              [pretty-inputs.engine.api                  :as pretty-inputs.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-error-text-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ _]
  (-> {:class :pi-header--error-text}
      (pretty-css.content/font-attributes {:font-size :xs :letter-spacing :auto :line-height :text-block :font-weight :normal})
      (pretty-css.content/text-attributes {:text-color :warning})))

(defn header-helper-text-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ _]
  (-> {:class :pi-header--helper-text}
      (pretty-css.content/font-attributes {:font-size :xs :letter-spacing :auto :line-height :text-block :font-weight :normal})
      (pretty-css.content/text-attributes {:text-color :muted})))

(defn header-info-text-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ _]
  (-> {:class :pi-header--info-text}
      (pretty-css.content/font-attributes {:font-size :xs :letter-spacing :auto :line-height :text-block :font-weight :normal})
      (pretty-css.content/text-attributes {:text-color :muted})))

(defn header-label-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :on-mouse-up (function)}
  [header-id header-props]
  (let [on-mouse-up-f (pretty-inputs.engine/focus-input! header-id)]
       (-> {:class :pi-header--content
            :on-mouse-up on-mouse-up-f}
           (pretty-css.content/font-attributes              {:font-size :s :letter-spacing :auto :line-height :text-block :font-weight :medium})
           (pretty-css.content/unselectable-text-attributes {})
           (pretty-css.layout/flex-attributes               {:orientation :horizontal}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-body-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ header-props]
  (-> {:class :pi-header--body}
      (pretty-css.basic/style-attributes            header-props)
      (pretty-css.layout/full-block-size-attributes header-props)
      (pretty-css.layout/indent-attributes          header-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {}
  [_ header-props]
  (-> {:class :pi-header}
      (pretty-css.appearance/theme-attributes    header-props)
      (pretty-css.basic/class-attributes         header-props)
      (pretty-css.basic/state-attributes         header-props)
      (pretty-css.layout/outdent-attributes      header-props)
      (pretty-css.layout/wrapper-size-attributes header-props)))
