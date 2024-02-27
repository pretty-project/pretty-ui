
(ns pretty-layouts.surface.prototypes
    (:require [pretty-rules.api :as pretty-rules]
              [pretty-properties.api :as pretty-properties]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]
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
  [popup-id {:keys [footer] :as popup-props}]
  (if (pretty-layouts.engine/layout-footer-overlapping? popup-id popup-props)
      (-> footer (pretty-properties/default-border-props {:border-color :default     :border-position :top :border-width :xxs}))
      (-> footer (pretty-properties/default-border-props {:border-color :transparent :border-position :top :border-width :xxs}))))

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
  [popup-id {:keys [header] :as popup-props}]
  (if (pretty-layouts.engine/layout-header-overlapping? popup-id popup-props)
      (-> header (pretty-properties/default-border-props {:border-color :default     :border-position :bottom :border-width :xxs}))
      (-> header (pretty-properties/default-border-props {:border-color :transparent :border-position :bottom :border-width :xxs}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-props-prototype
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  [_ surface-props]
  (-> surface-props (pretty-properties/default-content-size-props   {:content-height :grow :content-width :parent})
                    (pretty-properties/default-flex-props           {:orientation :vertical})
                    ;(pretty-properties/default-inner-position-props {:inner-position :center :inner-position-method :flex})
                    ;(pretty-properties/default-outer-position-props {:outer-position :left :outer-position-method :absolute})
                    (pretty-properties/default-outer-size-props     {:outer-height :parent :outer-size-unit :screen :outer-width :parent})
                    ;(pretty-properties/default-overflow-props       {:vertical-overflow :scroll})
                    (pretty-standards/standard-animation-props)
                    (pretty-standards/standard-inner-position-props)
                    (pretty-standards/standard-inner-size-props)
                    (pretty-standards/standard-outer-position-props)
                    (pretty-standards/standard-outer-size-props)
                   ;(pretty-rules/auto-disable-highlight-color)
                   ;(pretty-rules/auto-disable-hover-color)
                    (pretty-subitems/subitems<-disabled-state :body :footer :header)
                    (pretty-subitems/leave-disabled-state     :body :footer :header)))