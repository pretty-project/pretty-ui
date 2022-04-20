
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.mount.events
    (:require [mid-fruits.map                      :refer [dissoc-in]]
              [plugins.item-lister.core.events     :as core.events]
              [plugins.plugin-handler.mount.events :as mount.events]
              [x.app-core.api                      :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.mount.events
(def store-body-props!    mount.events/store-body-props!)
(def store-header-props!  mount.events/store-header-props!)
(def remove-body-props!   mount.events/remove-body-props!)
(def remove-header-props! mount.events/remove-header-props!)
(def update-body-props!   mount.events/update-body-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ lister-id header-props]]
  (r store-header-props! db lister-id header-props))

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;  {:reorder-mode? (boolean)(opt)
  ;   :select-mode? (boolean)(opt)}
  ;
  ; @return (map)
  [db [_ lister-id {:keys [reorder-mode? select-mode?] :as body-props}]]
  ; Az item-lister plugin {:reorder-mode? ...} és {:select-mode? ...} állapotának kezdeti értéke
  ; a body komponens paramétereként is átadható, így a header komponensen használata nélkül is
  ; 
  (cond-> db :store-body-props!     (as-> % (r store-body-props!                 % lister-id body-props))
             :set-default-order-by! (as-> % (r core.events/set-default-order-by! % lister-id))
             (some? reorder-mode?)  (as-> % (r core.events/set-meta-item!        % lister-id :reorder-mode? reorder-mode?))
             (some? select-mode?)   (as-> % (r core.events/set-meta-item!        % lister-id :select-mode?  select-mode?))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (r remove-header-props! db lister-id))

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (as-> db % (r core.events/reset-meta-items! % lister-id)
             (r core.events/reset-downloads!  % lister-id)
             (r remove-body-props!            % lister-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ lister-id body-props]]
  (as-> db % (r core.events/reset-downloads!  % lister-id)
             (r core.events/reset-selections! % lister-id)
             (r update-body-props!            % lister-id body-props)))
