
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.subs
    (:require [mid-fruits.logical    :refer [or=]]
              [mid-fruits.vector     :as vector]
              [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
              [x.app-ui.header.subs  :as header.subs]
              [x.app-ui.renderer     :as renderer]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-popup-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (keyword) prop-key
  ;
  ; @return (boolean)
  [db [_ popup-id prop-key]]
  (r renderer/get-element-prop db :popups popup-id prop-key))

(defn render-touch-anchor?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (boolean)
  [db [_ popup-id]]
  (let [layout (r renderer/get-element-prop db :popups popup-id :layout)]
       (and (or= layout :boxed :flip)
            (r environment/viewport-small? db)
            (r environment/touch-detected? db))))

(defn render-popup-cover?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (boolean)
  [db [_ popup-id]]
  (or (r renderer/any-element-rendered? db :surface)
      (r header.subs/render-header?     db)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/get-popup-prop get-popup-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/render-touch-anchor? render-touch-anchor?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/render-popup-cover? render-popup-cover?)
