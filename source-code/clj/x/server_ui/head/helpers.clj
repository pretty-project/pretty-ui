
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.head.helpers
    (:require [mid-fruits.candy         :refer [param return]]
              [mid-fruits.string        :as string]
              [mid-fruits.vector        :as vector]
              [server-fruits.http       :as http]
              [time.api                 :as time]
              [x.app-details            :as details]
              [x.server-core.api        :as a :refer [cache-control-uri]]
              [x.server-router.api      :as router]
              [x.server-ui.core.helpers :refer [include-css include-favicon include-font]]
              [x.server-ui.head.config  :as head.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn meta-keywords->formatted-meta-keywords
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string or strings in vector) meta-keywords
  ;
  ; @example
  ;  (meta-keywords->formatted-meta-keywords ["My" "keywords"])
  ;  =>
  ;  "My, keywords"
  ;
  ; @example
  ;  (meta-keywords->formatted-meta-keywords "My, keywords")
  ;  =>
  ;  "My, keywords"
  ;
  ; @return (string)
  [meta-keywords]
  (if (string?     meta-keywords)
      (return      meta-keywords)
      (string/join meta-keywords ", ")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn css-paths
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) head-props
  ;  {:css-paths (maps in vector)(opt)}
  ;
  ; @return (maps in vector)
  [request head-props]
  ; XXX#5061
  ; A head elemben felsorolt CSS fájlok forrásáról bővebben a modul
  ; README.md fájljában olvashatsz!
  (let [app-config            @(a/subscribe [:core/get-app-config])
        environment-css-paths @(a/subscribe [:environment/get-css-paths])]
       (vector/concat-items head.config/SYSTEM-CSS-PATHS
                            environment-css-paths
                            (:css-paths app-config)
                            (:css-paths head-props))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn include-css?
  ; @param (map) request
  ; @param (map) head-props
  ;  {:core-js (string)}
  ; @param (map) css-props
  ;  {:core-js (string)(opt)
  ;   :route-template (string)(opt)}
  ;
  ; @return (boolean)
  [request {:keys [core-js]} {:keys [route-template] :as css-props}]
  ; XXX#1720
  ; A head komponensben felsorolt CSS fájlok abban az esetben lesznek ténylegesen
  ; alkalmazva, ha a CSS fájlt leíró css-props térképben ...
  ; ... nincs meghatározva, hogy a CSS fájl melyik core-js fájl használata esetén
  ;     legyen alkalmazva, vagy a css-props térképben meghatároztt core-js fájl
  ;     megegyezik az aktuálisan használatban lévő core-js fájllal.
  ; ... nincs meghatározva, hogy a CSS fájl milyen útonal használata esetén legyen
  ;     alkalmazva, vagy az aktuálisan használt útvonal illeszkedik a css-props
  ;     térképben meghatározott route-template mintával.
  (and (or (-> css-props :core-js nil?)
           (-> css-props :core-js (= core-js)))
       (or (-> css-props :route-template nil?)
           (router/request->route-template-matched? request route-template))))

(defn include-favicon?
  ; @param (map) request
  ; @param (map) head-props
  ;  {:core-js (string)}
  ; @param (map) favicon-props
  ;  {:core-js (string)(opt)
  ;   :route-template (string)(opt)}
  ;
  ; @return (boolean)
  [request {:keys [core-js]} {:keys [route-template] :as favicon-props}]
  ; XXX#1720
  (and (or (-> favicon-props :core-js nil?)
           (-> favicon-props :core-js (= core-js)))
       (or (-> favicon-props :route-template nil?)
           (router/request->route-template-matched? request route-template))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn head<-crawler-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:crawler-rules (string)
  ;   :meta-description (string)
  ;   :meta-keywords (string or strings in vector)
  ;   :meta-name (string)
  ;   :meta-title (string)}
  ;
  ; @return (hiccup)
  [head _ {:keys [crawler-rules meta-description meta-keywords meta-name meta-title]}]
  (let [meta-keywords (meta-keywords->formatted-meta-keywords meta-keywords)]
       (vector/concat-items head [[:meta {:content crawler-rules    :name "robots"}]
                                  [:meta {:content meta-description :name "description"}]
                                  [:meta {:content meta-title       :name "title"}]])))

(defn head<-share-preview-properties
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:meta-description (string)
  ;   :meta-name (string)
  ;   :share-preview-uri (string)}
  ;
  ; @return (hiccup)
  [head _ {:keys [meta-description meta-name share-preview-uri]}]
  (vector/concat-items head [[:meta {:content meta-name         :itemprop "name"}]
                             [:meta {:content meta-description  :itemprop "description"}]
                             [:meta {:content share-preview-uri :itemprop "image"}]]))

(defn head<-browser-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-title (string)
  ;   :selected-language (keyword)
  ;   :theme-color (string)}
  ;
  ; @return (hiccup)
  [head _ {:keys [app-title selected-language theme-color]}]
  (vector/concat-items head [[:title app-title]
                             [:meta {:charset "utf-8"}]
                             ; maximum-scale=1
                             ; A mobileszköz böngészők ne nagyítsák a tartalmat input elemek kitöltése közben.
                             ; https://stackoverflow.com/questions/2989263/disable-auto-zoom-in-input-text-tag-safari-on-iphone
                             [:meta {:content "width=320, initial-scale=1 maximum-scale=1" :name "viewport"}]
                             [:meta {:content theme-color                                  :name "theme-color"}]
                             [:meta {:content selected-language                            :http-equiv "content-language"}]]))

(defn head<-legal-information
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:author (string)}
  ;
  ; @return (hiccup)
  [head _ {:keys [author]}]
  (let [current-year          (time/get-year)
        copyright-information (details/copyright-information current-year)]
       (vector/concat-items head [[:meta {:content author                :name "author"}]
                                  [:meta {:content details/app-version   :name "version"}]
                                  [:meta {:content copyright-information :name "copyright"}]])))

(defn head<-og-properties
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; The Open Graph protocol
  ; https://ogp.me/
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-title (string)
  ;   :meta-description (string)
  ;   :share-preview-uri (string)}
  ;
  ; @return (hiccup)
  [head request {:keys [app-title meta-description share-preview-uri]}]
  (let [og-url (http/request->uri request)]
       (vector/concat-items head [[:meta {:content "website"         :property "og:type"}]
                                  [:meta {:content meta-description  :property "og:description"}]
                                  [:meta {:content app-title         :property "og:title"}]
                                  [:meta {:content share-preview-uri :property "og:image"}]
                                  [:meta {:content og-url            :property "og:url"}]])))

(defn head<-css-includes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-build (string)(opt)}
  ;
  ; @return (hiccup)
  [head request {:keys [app-build] :as head-props}]
  (letfn [(f [head {:keys [uri] :as css-props}]
             (let [cache-control-uri (cache-control-uri uri app-build)
                   css-props         (assoc css-props :uri cache-control-uri)]
                  (if (include-css? request head-props css-props)
                      (conj   head (include-css css-props))
                      (return head))))]
         (let [css-paths (css-paths request head-props)]
              (reduce f head css-paths))))

(defn head<-favicon-includes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-build (string)(opt)
  ;   :favicon-paths (maps in vector)
  ;    [{:core-js (string)(opt)
  ;      :size (string)
  ;      :uri (string)}]
  ;
  ; @return (hiccup)
  [head request {:keys [app-build favicon-paths] :as head-props}]
  (letfn [(f [head {:keys [uri] :as favicon-props}]
             (let [cache-control-uri (cache-control-uri uri app-build)
                   favicon-props     (assoc favicon-props :uri cache-control-uri)]
                  (if (include-favicon? request head-props favicon-props)
                      (conj   head (include-favicon favicon-props))
                      (return head))))]
         (reduce f head favicon-paths)))
