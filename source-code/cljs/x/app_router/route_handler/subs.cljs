
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.subs
    (:require [candy.api                         :refer [return]]
              [mid-fruits.string                 :as string]
              [mid-fruits.uri                    :as uri]
              [mid-fruits.vector                 :as vector]
              [mid.x.router.route-handler.subs   :as route-handler.subs]
              [reitit.frontend                   :as reitit.frontend]
              [re-frame.api                      :as r :refer [r]]
              [x.app-core.api                    :as x.core]
              [x.app-router.route-handler.config :as route-handler.config]
              [x.app-user.api                    :as x.user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.router.route-handler.subs
(def get-app-home   route-handler.subs/get-app-home)
(def use-app-home   route-handler.subs/use-app-home)
(def get-app-domain route-handler.subs/get-app-domain)
(def use-app-domain route-handler.subs/use-app-domain)



;; -- Debug subscriptions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-debug-mode
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (string)
  [db [_ route-string]]
  (if-let [debug-mode (r x.core/get-debug-mode db)]
          (uri/uri<-query-string route-string debug-mode)
          (return                route-string)))



;; -- Router subscriptions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-routes
  ; @return (map)
  [db _]
  (get-in db [:router :route-handler/client-routes]))

(defn get-ordered-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vectors in vector)
  ;  [[(string) route-template
  ;    (keyword) route-id]
  ;   [...]
  ;   [...]]
  [db _]
  (get-in db [:router :route-handler/ordered-routes]))

(defn get-router
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (?)
  [db _]
  ; XXX#4006 (source-code/clj/x/server_core/router_handler/helpers.clj)
  ; A szerver-oldali útvonal-kezelőhöz hasonlóan a kliens-oldalon is szükséges
  ; átadni a {:conflicts nil} beállítást.
  (let [ordered-routes (r get-ordered-routes db)]
       (reitit.frontend/router ordered-routes {:conflicts nil})))

(defn get-route-match
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-template
  ;
  ; @return (map)
  ;  https://github.com/metosin/reitit
  [db [_ route-template]]
  (let [router (r get-router db)]
       (reitit.frontend/match-by-path router route-template)))

(defn route-template-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (boolean)
  [db [_ route-string]]
  (let [route-match (r get-route-match db route-string)]
       (boolean route-match)))

(defn swap-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  ; XXX#0781 (source-code/cljs/x/app_router/route_handler/effects.cljs)
  (get-in db [:router :route-handler/meta-items :swap-mode?]))



;; -- Route subscriptions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-restricted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (let [route-restricted? (get-in db [:router :route-handler/client-routes route-id :restricted?])]
       (boolean route-restricted?)))

(defn require-authentication?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (and (r route-restricted?         db route-id)
       (r x.user/user-unidentified? db)))



;; -- Match subscriptions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn match-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (keyword)
  [db [_ route-string]]
  (if (r route-template-exists? db route-string)
      (let [route-match (r get-route-match db route-string)]
           (get-in route-match [:data :name]))
      (return :page-not-found)))



;; -- Current route subscriptions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-route-string
  ; @usage
  ;  (r get-current-route-string db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:router :route-handler/meta-items :route-string]))

(defn get-current-route-id
  ; @usage
  ;  (r get-current-route-id db)
  ;
  ; @return (keyword)
  [db _]
  (get-in db [:router :route-handler/meta-items :route-id]))

(defn get-current-route-path
  ; @usage
  ;  (r get-current-route-path db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:router :route-handler/meta-items :route-path]))

(defn get-current-route-template
  ; @usage
  ;  (r get-current-route-template db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-id (r get-current-route-id db)]
       (get-in db [:router :route-handler/client-routes current-route-id :route-template])))

(defn get-current-js-build
  ; @usage
  ;  (r get-current-js-build db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-id (r get-current-route-id db)]
       (get-in db [:router :route-handler/client-routes current-route-id :js-build])))

(defn get-current-route-path-params
  ; @usage
  ;  (r get-current-route-path-params db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:router :route-handler/meta-items :route-path-params]))

(defn get-current-route-path-param
  ; @param (keyword) param-key
  ;
  ; @usage
  ;  (r get-current-route-path-param db :my-param)
  ;
  ; @return (string)
  [db [_ param-key]]
  (get-in db [:router :route-handler/meta-items :route-path-params param-key]))

(defn current-route-path-param?
  ; @param (keyword) param-key
  ; @param (string) param-value
  ;
  ; @usage
  ;  (r current-route-path-param? db :my-param "My value")
  ;
  ; @return (boolean)
  [db [_ param-key param-value]]
  (let [current-route-path-param (r get-current-route-path-param db param-key)]
       (= current-route-path-param param-value)))

(defn get-current-route-query-params
  ; @usage
  ;  (r get-current-route-query-params db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:router :route-handler/meta-items :route-query-params]))

(defn get-current-route-query-param
  ; @param (keyword) param-key
  ;
  ; @usage
  ;  (r get-current-route-query-param db :my-param)
  ;
  ; @return (string)
  [db [_ param-key]]
  (get-in db [:router :route-handler/meta-items :route-query-params param-key]))

(defn current-route-query-param?
  ; @param (keyword) param-key
  ; @param (string) param-value
  ;
  ; @usage
  ;  (r current-route-query-param? db :my-param "My value")
  ;
  ; @return (boolean)
  [db [_ param-key param-value]]
  (let [current-route-query-param (r get-current-route-query-param db param-key)]
       (= current-route-query-param param-value)))

(defn get-current-route-fragment
  ; @usage
  ;  (r get-current-route-fragment db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:router :route-handler/meta-items :route-fragment]))

(defn get-current-route-parent
  ; @usage
  ;  (r get-current-route-parent db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:router :route-handler/meta-items :route-parent]))

(defn use-path-params
  ; @param (string) uri
  ; @param (map)(opt) path-params
  ;  A path-params paraméter hiányában a függvény az aktuális útvonalból
  ;  származtatott útvonal-paramétereket használja behelyettesítő értékekként.
  ;
  ; @usage
  ;  (r use-path-params db "/@app-home/my-route/:my-param/xxx" {:my-param "my-value"})
  ;  =>
  ;  "/@app-home/my-route/my-value/xxx"
  ;
  ; @return (string)
  [db [_ uri path-params]]
  (let [path-params (or path-params (r get-current-route-path-params db))]
       (letfn [; 2. Az f0 függvény az n paraméterként kapott szöveget felbontja (param-key, trail)
               ;    a "/" karakter első előfordulásánál és egy vektorban felsorolva visszatér
               ;    a két felbontott részlettel (az első részletből kulcsszó típust készít)
               ;    Pl.: n: ":my-param/xxx"
               ;         =>
               ;         [:my-param "/xxx"]
               (f0 [n] (if-let [pos (string/first-dex-of n "/")]
                               [(keyword (subs n 1 pos))
                                (str     (subs n   pos))]
                               [(keyword (subs n 1)) ""]))
               ; 1. Az f1 függvény az n paraméterként kapott szöveget felbontja (base, end),
               ;    a "/:" részlet első előfordulásánál.
               ;    Pl.: n:    "/@app-home/my-route/:my-param/xxx"
               ;         =>
               ;         base: "/@app-home/my-route/"
               ;         end:  ":my-param/xxx"
               ;
               ; 3. Az f1 függvény n paramétereként kapott szövegben az első útvonal-paramétert
               ;    behelyettesíti az aktuális útvonalbeli értékével (ha lehetséges),
               ;    majd a behelyettesítés után az eredménnyel újra meghívja önmagát, további
               ;    útvonal-paramétereket keresve a szövegben.
               (f1 [n cursor]
                   (if-let [pos (-> n (string/part cursor)
                                      (string/first-dex-of "/:"))]
                           ; Ha az f függvény n paramétere tartalmazza a "/:" részletet ...
                           (let [base (subs n 0 (inc pos))
                                 end  (subs n   (inc pos))
                                 [param-key trail] (f0 end)]
                                (if-let [param-value (get path-params param-key)]
                                        (f1 (str base param-value trail) cursor)
                                        (f1 (str base param-key   trail) cursor)))
                           ; Ha az f függvény n paramétere NEM tartalmazza az "/:" részletet ...
                           (return n)))]
              (f1 uri 0))))



;; -- Handle subscriptions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn at-home?
  ; @usage
  ;  (r at-home? db)
  ;
  ; @return (boolean)
  [db _]
  (let [app-home           (r get-app-home           db)
        current-route-path (r get-current-route-path db)]
       (= current-route-path app-home)))

(defn home-next-door?
  ; @usage
  ;  (r home-next-door? db)
  ;
  ; @return (boolean)
  [db _]
  (= (r get-app-home             db)
     (r get-current-route-parent db)))

(defn route-id-unchanged?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (let [current-route-id (r get-current-route-id db)]
       (= route-id current-route-id)))

(defn route-id-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (let [current-route-id (r get-current-route-id db)]
       (not= route-id current-route-id)))



;; -- History subscriptions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-history
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keywords in vector)
  [db _]
  (get-in db [:router :route-handler/meta-items :history]))

(defn get-previous-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (let [history (r get-history db)]
       (vector/last-item history)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :router/get-client-routes get-client-routes)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :router/route-template-exists? route-template-exists?)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :router/match-route-id match-route-id)

; @usage
;  [:router/get-current-route-string]
(r/reg-sub :router/get-current-route-string get-current-route-string)

; @usage
;  [:router/get-current-route-id]
(r/reg-sub :router/get-current-route-id get-current-route-id)

; @usage
;  [:router/get-current-route-path]
(r/reg-sub :router/get-current-route-path get-current-route-path)

; @usage
;  [:router/get-current-route-template]
(r/reg-sub :router/get-current-route-template get-current-route-template)

; @usage
;  [:router/get-current-js-build]
(r/reg-sub :router/get-current-js-build get-current-js-build)

; @usage
;  [:router/get-current-route-path-params]
(r/reg-sub :router/get-current-route-path-params get-current-route-path-params)

; @usage
;  [:router/get-current-route-path-param :my-param]
(r/reg-sub :router/get-current-route-path-param get-current-route-path-param)

; @usage
;  [:router/current-route-path-param? :my-param "My value"]
(r/reg-sub :router/current-route-path-param? current-route-path-param?)

; @usage
;  [:router/get-current-route-query-param]
(r/reg-sub :router/get-current-route-query-params get-current-route-query-params)

; @usage
;  [:router/get-current-route-query-param :my-param]
(r/reg-sub :router/get-current-route-query-param get-current-route-query-param)

; @usage
;  [:router/current-route-query-param? :my-param "My value"]
(r/reg-sub :router/current-route-query-param? current-route-query-param?)

; @usage
;  [:router/get-current-route-fragment]
(r/reg-sub :router/get-current-route-fragment get-current-route-fragment)

; @usage
;  [:router/get-current-route-parent]
(r/reg-sub :router/get-current-route-parent get-current-route-parent)

; @usage
;  [:router/at-home?]
(r/reg-sub :router/at-home? at-home?)

; @usage
;  [:router/home-next-door?]
(r/reg-sub :router/home-next-door? home-next-door?)
