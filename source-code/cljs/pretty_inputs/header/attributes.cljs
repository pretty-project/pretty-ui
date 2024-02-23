
(ns pretty-inputs.header.attributes
    (:require [pretty-attributes.api    :as pretty-attributes]
              [pretty-inputs.engine.api :as pretty-inputs.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-error-text-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ _]
  (-> {:class :pi-header--error-text}
      (pretty-attributes/font-attributes {:font-size :xs :letter-spacing :auto :line-height :text-block :font-weight :normal})
      (pretty-attributes/text-attributes {:text-color :warning})))

(defn header-helper-text-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ _]
  (-> {:class :pi-header--helper-text}
      (pretty-attributes/font-attributes {:font-size :xs :letter-spacing :auto :line-height :text-block :font-weight :normal})
      (pretty-attributes/text-attributes {:text-color :muted})))

(defn header-info-text-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ _]
  (-> {:class :pi-header--info-text}
      (pretty-attributes/font-attributes {:font-size :xs :letter-spacing :auto :line-height :text-block :font-weight :normal})
      (pretty-attributes/text-attributes {:text-color :muted})))

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
           (pretty-attributes/font-attributes {:font-size :s :letter-spacing :auto :line-height :text-block :font-weight :medium})
           (pretty-attributes/flex-attributes {:orientation :horizontal})
           (pretty-attributes/text-attributes {:text-selectable? false}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-body-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ header-props]
  (-> {:class :pi-header--body}
      (pretty-attributes/body-size-attributes header-props)
      (pretty-attributes/indent-attributes    header-props)
      (pretty-attributes/style-attributes     header-props)))

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
      (pretty-attributes/class-attributes   header-props)
      (pretty-attributes/outdent-attributes header-props)
      (pretty-attributes/size-attributes    header-props)
      (pretty-attributes/state-attributes   header-props)
      (pretty-attributes/theme-attributes   header-props)))
