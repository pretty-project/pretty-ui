
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.09.11
; Description:
; Version: v1.2.0
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.structure
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
              [x.app-ui.background   :rename {view app-background}]
              [x.app-ui.bubbles      :rename {view app-bubbles}]
              [x.app-ui.header       :rename {view app-header}]
              [x.app-ui.locker       :rename {view app-locker}]
              [x.app-ui.popups       :rename {view app-popups}]
              [x.app-ui.progress-bar :rename {view progress-bar}]
              [x.app-ui.surface      :rename {view app-surface}]
              [x.app-user.api        :as user]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ui-structure-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) structure-props
  ;  {:scrolled-to-top? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-scrolled-to-top (boolean)}
  [{:keys [scrolled-to-top?]}]
  (cond-> {}
          (some? scrolled-to-top?) (assoc :data-scrolled-to-top scrolled-to-top?)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-structure-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:client-locked? (boolean)
  ;   :scrolled-to-top? (boolean)}
  [db _]
  {:client-locked?   (r user/client-locked?          db)
   :scrolled-to-top? (r environment/scrolled-to-top? db)})

(a/reg-sub :ui/get-structure-props get-structure-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- locked-ui-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) structure-id
  ; @param (map) structure-props
  ;
  ; @return (hiccup)
  [_ structure-props]
  [:div#x-app-ui-structure (ui-structure-attributes structure-props)
                           [app-locker]])

(defn- unlocked-ui-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) structure-id
  ; @param (map) structure-props
  ;
  ; @return (hiccup)
  [_ structure-props]
  [:div#x-app-ui-structure (ui-structure-attributes structure-props)
                          ;[app-sounds]
                           [app-background]
                           [app-surface]
                           [app-header]
                           [app-popups]
                           [app-bubbles]
                           [progress-bar]])

(defn- client-lock-controller
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) structure-id
  ; @param (map) structure-props
  ;  {:client-locked? (boolean)}
  ;
  ; @return (hiccup)
  [structure-id {:keys [client-locked?] :as structure-props}]
  (if client-locked? [locked-ui-structure   structure-id structure-props]
                     [unlocked-ui-structure structure-id structure-props]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [components/subscriber ::view
                         {:render-f   #'client-lock-controller
                          :subscriber [:ui/get-structure-props]}])
