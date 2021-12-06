
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.09.11
; Description:
; Version: v1.0.8
; Compatibility: x4.4.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.structure
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
              [x.app-ui.background   :rename {view app-background}]
              [x.app-ui.bubbles      :rename {view app-bubbles}]
              [x.app-ui.canvas       :rename {view app-canvas}]
              [x.app-ui.header       :rename {view app-header}]
              [x.app-ui.interface    :as interface]
              [x.app-ui.locker       :rename {view app-locker}]
              [x.app-ui.popups       :rename {view app-popups}]
              [x.app-ui.progress-bar :rename {view progress-bar}]
              [x.app-ui.surface      :rename {view app-surface}]
              [x.app-ui.sidebar      :rename {view app-sidebar}]
              [x.app-user.api        :as user]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ui-structure-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:interface (keyword)(opt)
  ;   :scrolled-to-top? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-interface (string)
  ;   :data-scrolled-to-top (boolean)}
  [{:keys [interface scrolled-to-top?]}]
  (cond-> {} (some? interface)        (assoc :data-interface       interface)
             (some? scrolled-to-top?) (assoc :data-scrolled-to-top scrolled-to-top?)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:client-locked? (boolean)
  ;   :interface (keyword)}
  [db _]
  {:client-locked?   (r user/client-locked?          db)
   :interface        (r interface/get-interface      db)
   :scrolled-to-top? (r environment/scrolled-to-top? db)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- locked-ui-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) view-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ view-props]
  [:div#x-app-ui-structure (ui-structure-attributes view-props)
                           [app-locker]])

(defn- unlocked-ui-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) view-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ view-props]
  [:div#x-app-ui-structure (ui-structure-attributes view-props)
                          ;[app-sounds]
                           [app-background]
                           [app-surface]
                           [app-header]
                           [app-popups]
                           [app-sidebar]
                           [app-canvas]
                           [app-bubbles]
                           [progress-bar]])

(defn- client-lock-controller
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) view-id
  ; @param (map) view-props
  ;  {:client-locked? (boolean)}
  ;
  ; @return (hiccup)
  [view-id {:keys [client-locked?] :as view-props}]
  (if client-locked? [locked-ui-structure   view-id view-props]
                     [unlocked-ui-structure view-id view-props]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [components/subscriber ::view
                         {:component  #'client-lock-controller
                          :subscriber [::get-view-props]}])
