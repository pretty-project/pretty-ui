
(ns pretty-layouts.popup.prototypes
    (:require [pretty-rules.api :as pretty-rules]
              [pretty-properties.api :as pretty-properties]
              [pretty-standards.api :as pretty-standards]

              [pretty-layouts.engine.api :as pretty-layouts.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-props-prototype
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:footer (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [popup-id {:keys [footer]}]
  (if (pretty-layouts.engine/layout-footer-overlapping? popup-id {})
      (-> footer (assoc :fill-color :warning))
      (-> footer)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:body (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [_ {:keys [body]}]
  (-> body))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:header (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [popup-id {:keys [header]}]
  (if (pretty-layouts.engine/layout-header-overlapping? popup-id {})
      (-> header (assoc :fill-color :warning))
      (-> header)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  [_ popup-props]
  (-> popup-props (pretty-properties/default-flex-props           {:orientation :vertical})
                  (pretty-properties/default-outer-position-props {:outer-position :center :outer-position-method :fixed})
                  (pretty-properties/default-outer-size-props     {:outer-size-unit :screen})
                  (pretty-standards/standard-border-props)
                  (pretty-standards/standard-inner-position-props)
                  (pretty-standards/standard-inner-size-props)
                  (pretty-standards/standard-outer-position-props)
                  (pretty-standards/standard-outer-size-props)
                  (pretty-standards/standard-overlay-props)
                  (pretty-rules/apply-auto-border-crop)))
                 ;(pretty-rules/auto-disable-highlight-color)
                 ;(pretty-rules/auto-disable-hover-color)))
