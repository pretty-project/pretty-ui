
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.head.helpers
    (:require [candy.api          :refer [param return]]
              [http.api           :as http]
              [re-frame.api       :as r]
              [string.api         :as string]
              [time.api           :as time]
              [vector.api         :as vector]
              [x.app-details      :as x.app-details]
              [x.core.api         :refer [cache-control-uri]]
              [x.router.api       :as x.router]
              [x.ui.core.helpers  :refer [include-css include-favicon include-font]]
              [x.ui.head.config   :as head.config]))



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
  ; XXX#5061 (source-code/clj/x/ui/README.md)
  (let [app-config            @(r/subscribe [:x.core/get-app-config])
        environment-css-paths @(r/subscribe [:x.environment/get-css-paths])]
       (vector/concat-items head.config/SYSTEM-CSS-PATHS
                            environment-css-paths
                            (:css-paths app-config))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn include-css?
  ; @param (map) request
  ; @param (map) head-props
  ;  {:js-build (keyword)}
  ; @param (map) css-props
  ;  {:js-build (keyword)(opt)
  ;   :route-template (string)(opt)}
  ;
  ; @return (boolean)
  [request {:keys [js-build]} {:keys [route-template] :as css-props}]
  ; XXX#1720
  ; A head komponensben felsorolt CSS fájlok abban az esetben lesznek ténylegesen
  ; alkalmazva, ha a CSS fájlt leíró css-props térképben ...
  ; ... nincs meghatározva, hogy a CSS fájl melyik JS build használata esetén
  ;     legyen alkalmazva, vagy a css-props térképben meghatároztt JS build
  ;     megegyezik az aktuálisan használatban lévő JS build-el.
  ; ... nincs meghatározva, hogy a CSS fájl milyen útonal használata esetén legyen
  ;     alkalmazva, vagy az aktuálisan használt útvonal illeszkedik a css-props
  ;     térképben meghatározott route-template mintával.
  (and (or (-> css-props :js-build nil?)
           (-> css-props :js-build (= js-build)))
       (or (-> css-props :route-template nil?)
           (x.router/request->route-template-matched? request route-template))))

(defn include-favicon?
  ; @param (map) request
  ; @param (map) head-props
  ;  {:js-build (keyword)}
  ; @param (map) favicon-props
  ;  {:js-build (keyword)(opt)
  ;   :route-template (string)(opt)}
  ;
  ; @return (boolean)
  [request {:keys [js-build]} {:keys [route-template] :as favicon-props}]
  ; XXX#1720
  (and (or (-> favicon-props :js-build nil?)
           (-> favicon-props :js-build (= js-build)))
       (or (-> favicon-props :route-template nil?)
           (x.router/request->route-template-matched? request route-template))))



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
  ;   :theme-color (string)(opt)}
  ;
  ; @return (hiccup)
  [head _ {:keys [app-title selected-language theme-color]}]
  (vector/concat-items head [[:title app-title]
                             [:meta {:charset "utf-8"}]
                             ; maximum-scale=1
                             ; A mobileszköz böngészők ne nagyítsák a tartalmat input elemek kitöltése közben.
                             ; https://stackoverflow.com/questions/2989263/disable-auto-zoom-in-input-text-tag-safari-on-iphone
                             [:meta {:content "width=320, initial-scale=1 maximum-scale=1" :name "viewport"}]
                             (if theme-color [:meta {:content theme-color                  :name "theme-color"}])
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
        copyright-information (x.app-details/copyright-information current-year)]
       (vector/concat-items head [[:meta {:content author                    :name "author"}]
                                  [:meta {:content x.app-details/app-version :name "version"}]
                                  [:meta {:content copyright-information     :name "copyright"}]])))

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
  ;  {:build-version (string)(opt)}
  ;
  ; @return (hiccup)
  [head request {:keys [build-version] :as head-props}]
  (letfn [(f [head {:keys [uri] :as css-props}]
             (let [cache-control-uri (cache-control-uri uri build-version)
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
  ;  {:build-version (string)(opt)
  ;   :favicon-paths (maps in vector)
  ;    [{:js-build (keyword)(opt)
  ;      :size (string)
  ;      :uri (string)}]
  ;
  ; @return (hiccup)
  [head request {:keys [build-version favicon-paths] :as head-props}]
  (letfn [(f [head {:keys [uri] :as favicon-props}]
             (let [cache-control-uri (cache-control-uri uri build-version)
                   favicon-props     (assoc favicon-props :uri cache-control-uri)]
                  (if (include-favicon? request head-props favicon-props)
                      (conj   head (include-favicon favicon-props))
                      (return head))))]
         (reduce f head favicon-paths)))
