
; WARNING! EXPIRED! DO NOT USE!



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.23
; Description:
; Version: v0.5.6
; Compatibility:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.image-loader
    (:require [app-fruits.reagent :as reagent]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.css     :as css]
              [mid-fruits.map     :refer [dissoc-in]]
              [mid-fruits.keyword :as keyword]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (ns my-namespace (:require [x.app-tools.api :as tools]))
;  [tools/image-loader {:uri "/my-image.png"}]



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- container-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  ;  {:class (string or vector)
  ;   :image-loaded? (boolean)
  ;   :uri (string)}
  ;
  ; @return (map)
  ;  {:class (string or vector)
  ;   :id (keyword)
  ;   :style (map or nil)}
  [loader-id {:keys [class image-loaded? uri]}]
  {:class (param class)
   :id    (keyword/to-dom-value loader-id)
   :style (if image-loaded? {:backgroundImage (css/url uri)})})



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-loader-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (map)
  [db [_ loader-id]]
  (get-in db (db/path ::image-loaders loader-id)))

(a/reg-sub ::get-loader-props get-loader-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- init-image-loader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  ;
  ; @return (map)
  [db [_ loader-id loader-props]]
  (assoc-in db (db/path ::image-loaders loader-id) loader-props))

(a/reg-event-db :tools/init-image-loader! init-image-loader!)

(defn- destruct-image-loader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (map)
  [db [_ loader-id]]
  (dissoc-in db (db/path ::image-loaders loader-id)))

(a/reg-event-db :tools/destruct-image-loader! destruct-image-loader!)



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->image-loader-loaded
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  [db [_ loader-id]]
  (assoc-in db (db/path ::image-loaders loader-id :image-loaded?) true))

(a/reg-event-db :tools/->image-loader-loaded ->image-loader-loaded)

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :tools/->image-loader-updated
  (fn [_ [_ loader-id]]))
  ; uri megváltozott?



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- loading-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  ;  {:image-loaded? (boolean)}
  ;
  ; @return (hiccup)
  [loader-id {:keys [image-loaded?]}]
  (if (not image-loaded?)
      [:div.x-column {:data-vertical-align (keyword/to-dom-value :center)}
                     [:i.x-icon            (keyword/to-dom-value :image)]]))

(defn- control-image
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  ;  {:uri (string)}
  ;
  ; @return (hiccup)
  [loader-id {:keys [uri]}]
  [:img {:on-load #(a/dispatch [:tools/->image-loader-loaded loader-id])
         :src      (param uri)
         :style    {:display "none"}}])

(defn- image-loader
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  ;
  ; @return (hiccup)
  [loader-id loader-props]
  [:div (container-attributes loader-id loader-props)
        [control-image        loader-id loader-props]
        [loading-icon         loader-id loader-props]])

(defn component
  ; @param (keyword)(opt) loader-id
  ; @param (map) loader-props
  ;  {:class (string or vector)(opt)
  ;   :uri (string)}
  ;
  ; @usage
  ;  [tools/image-loader {...}]
  ;
  ; @usage
  ;  [tools/image-loader :my-loader {...}]
  ;
  ; @return (component)
  ([loader-props]
   [component (a/id) loader-props])

  ([loader-id loader-props]
   (reagent/lifecycles {:component-did-mount    #(a/dispatch [:tools/init-image-loader!     loader-id loader-props])
                        :component-did-update   #(a/dispatch [:tools/->image-loader-updated loader-id loader-props])
                        :component-will-unmount #(a/dispatch [:tools/destruct-image-loader! loader-id])
                        :reagent-render (fn [] (let [loader-props (a/subscribe [::get-loader-props loader-id])]
                                                    [image-loader loader-id @loader-props]))})))
