
(ns app-extensions.website-components.menu
    (:require [mid-fruits.candy     :refer [param]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-db.api         :as db]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) menu-props
  ;
  ; @return (map)
  ;  {:animation-name (keyword)}
  [menu-props]
  (merge {:animation-name :slide-in-rtl}
         (param menu-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:menu-visible? (boolean)
  [db _]
  {:menu-visible? (get-in db (db/meta-item-path ::primary :menu-visible?))})

(a/reg-sub :website-menu/get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-visibility!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (update-in db (db/meta-item-path ::primary :menu-visible?) not))

(a/reg-event-db :website-menu/toggle-visibility! toggle-visibility!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) menu-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [menu-id view-props]
  [:div#x-website-menu--content
    [components/content menu-id view-props]])

(defn- menu-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) menu-id
  ; @param (map) view-props
  ;  {:animation-name (keyword)
  ;   :menu-visible? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [menu-id {:keys [animation-name menu-visible?] :as view-props}]
  [:div#x-website-menu--body
    {:data-animation (a/dom-value animation-name)
     :data-visible   (boolean menu-visible?)}
    [menu-content menu-id view-props]])

(defn- menu-toggle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) menu-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ _]
  [:button#x-website-menu--toggle
    {:on-click    #(a/dispatch [:website-menu/toggle-visibility!])
     :on-mouse-up #(a/dispatch [:environment/blur-element!])
     :title        (components/content {:content :menu})}
    [:div#x-website-menu--toggle-icon]])

(defn- menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) menu-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [menu-id view-props]
  [:div#x-website-menu
    [menu-body   menu-id view-props]
    [menu-toggle menu-id view-props]])

(defn view
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;  {:animation-name (keyword)(opt)
  ;    :reveal, :slide-in-rtl
  ;    Default: :slide-in-rtl
  ;   :content (metamorphic-content)
  ;   :content-props (map)(opt)}
  ;
  ; @return (component)
  [menu-id menu-props]
  (let [menu-props (menu-props-prototype menu-props)]
       [components/subscriber menu-id {:base-props menu-props
                                       :component  #'menu
                                       :subscriber [:website-menu/get-view-props]}]))
